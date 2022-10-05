package com.soin.sgrm.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.model.Errors;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.ReleaseError;
import com.soin.sgrm.model.Releases_WithoutObj;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.SystemUser;
import com.soin.sgrm.service.ErrorService;
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.service.ReleaseErrorService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.JsonSheet;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/management/release")
public class ReleaseManagementController extends BaseController {

	public static final Logger logger = Logger.getLogger(ReleaseManagementController.class);

	@Autowired
	private StatusService statusService;
	@Autowired
	private ReleaseService releaseService;
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private ErrorService errorService;
	
	@Autowired
	private ReleaseErrorService releaseErrorService;
	
	@Autowired
	private ProjectService projectService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			loadCountsRelease(request, getUserLogin().getUsername());
			model.addAttribute("system", new SystemUser());
			model.addAttribute("systems", systemService.listSystemUser());
			model.addAttribute("status", new Status());
			model.addAttribute("statuses", statusService.list());
			model.addAttribute("errors",errorService.findAll());
		} catch (Exception e) {
			Sentry.capture(e, "releaseManagement");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina inicial/systemas." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return "/release/releaseManagement";

	}

	@RequestMapping(path = "/systemRelease", method = RequestMethod.GET)
	public @ResponseBody JsonSheet<?> getSystemRelease(HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
		try {
			String range = request.getParameter("dateRange");
			String[] dateRange = (range != null) ? range.split("-") : null;
			Integer systemId = Integer.parseInt(request.getParameter("systemId"));
			Integer statusId = Integer.parseInt(request.getParameter("statusId"));

			String name = getUserLogin().getUsername(), sSearch = request.getParameter("sSearch");
			int sEcho = Integer.parseInt(request.getParameter("sEcho")),
					iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart")),
					iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			return releaseService.listByAllSystem(name, sEcho, iDisplayStart, iDisplayLength, sSearch, null, dateRange,
					systemId, statusId);
		} catch (Exception e) {
			Sentry.capture(e, "releaseManagement");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}

	@RequestMapping(value = "/cancelRelease", method = RequestMethod.GET)
	public @ResponseBody JsonResponse cancelRelease(HttpServletRequest request, Model model,
			@RequestParam(value = "idRelease", required = true) Integer idRelease) {
		JsonResponse res = new JsonResponse();
		try {
			ReleaseEdit release = releaseService.findEditById(idRelease);
			Status status = statusService.findByName("Anulado");
			release.setStatus(status);
			release.setOperator(getUserLogin().getFullName());
			release.setMotive(status.getMotive());
			releaseService.updateStatusRelease(release);
			res.setStatus("success");

		} catch (SQLException ex) {
			Sentry.capture(ex, "releaseManagement");
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "releaseManagement");
			res.setStatus("exception");
			res.setException("Error al cancelar el release: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/statusRelease", method = RequestMethod.GET)
	public @ResponseBody JsonResponse draftRelease(HttpServletRequest request, Model model,
			@RequestParam(value = "idRelease", required = true) Integer idRelease,
			@RequestParam(value = "idStatus", required = true) Integer idStatus,
			@RequestParam(value = "dateChange", required = false) String dateChange,
			@RequestParam(value = "motive", required = true) String motive,
			@RequestParam(value = "idError", required = false) Long idError
			) {
		JsonResponse res = new JsonResponse();
		try {
			ReleaseEdit release = releaseService.findEditById(idRelease);
			Status status = statusService.findById(idStatus);
			if (status != null && status.getName().equals("Borrador")) {
				if (release.getStatus().getId() != status.getId())
					release.setRetries(release.getRetries() + 1);
			}else if(status != null && status.getName().equals("Error")) {
				Errors error=errorService.findById(idError);
				ReleaseError releaseError=new ReleaseError();
				releaseError.setSystem(release.getSystem());
				releaseError.setProject(projectService.findById(release.getSystem().getProyectId()));
				releaseError.setError(error);
				Releases_WithoutObj releaseWithObj=releaseService.findReleaseWithouObj(release.getId());
				releaseError.setRelease(releaseWithObj);
				releaseError.setObservations(motive);
				releaseError.setErrorDate(CommonUtils.getSystemTimestamp());
				releaseErrorService.save(releaseError);
				
				releaseService.updateStatusRelease(release);
				Status statusChange = statusService.findByName("Borrador");
				release.setStatus(statusChange);
				release.setOperator(getUserLogin().getFullName());

				release.setDateChange(dateChange);
				
				release.setMotive(motive);
				if (statusChange != null && statusChange.getName().equals("Borrador")) {
					if (release.getStatus().getId() != status.getId())
						release.setRetries(release.getRetries() + 1);
				}
				status=statusChange;
				motive="Paso a borrador por "+error.getName();
			}
			release.setStatus(status);
			release.setOperator(getUserLogin().getFullName());

			release.setDateChange(dateChange);
			release.setMotive(motive);
			releaseService.updateStatusRelease(release);
			res.setStatus("success");

		} catch (SQLException ex) {
			Sentry.capture(ex, "releaseManagement");
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			Sentry.capture(e, "releaseManagement");
			res.setStatus("exception");
			res.setException("Error al cambiar estado del release: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	public void loadCountsRelease(HttpServletRequest request, String name) {
		Map<String, Integer> systemC = new HashMap<String, Integer>();
		systemC.put("draft", releaseService.countByType(name, "Borrador", 3, null));
		systemC.put("requested", releaseService.countByType(name, "Solicitado", 3, null));
		systemC.put("completed", releaseService.countByType(name, "Completado", 3, null));
		systemC.put("all", (systemC.get("draft") + systemC.get("requested") + systemC.get("completed")));
		request.setAttribute("systemC", systemC);
	}

}

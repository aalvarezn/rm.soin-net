package com.soin.sgrm.controller;

import java.awt.print.Printable;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
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

import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.Errors_Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.ReleaseError;
import com.soin.sgrm.model.Releases_WithoutObj;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.SystemUser;
import com.soin.sgrm.security.UserLogin;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.ErrorReleaseService;
import com.soin.sgrm.service.ParameterService;
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
	private ErrorReleaseService errorService;

	@Autowired
	private ReleaseErrorService releaseErrorService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	ParameterService parameterService;

	@Autowired
	EmailTemplateService emailService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			loadCountsRelease(request, getUserLogin().getUsername());
			model.addAttribute("system", new SystemUser());
			model.addAttribute("systems", systemService.listSystemUser());
			model.addAttribute("status", new Status());
			model.addAttribute("statuses", statusService.list());
			model.addAttribute("errors", errorService.findAll());
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
			@RequestParam(value = "idError", required = false) Long idError,
			@RequestParam(value = "sendEmail", required = true) boolean sendEmail,
			@RequestParam(value = "senders", required = false) String senders) {
		JsonResponse res = new JsonResponse();
		try {
			ReleaseEdit release = releaseService.findEditById(idRelease);
			Status status = statusService.findById(idStatus);
			UserLogin userLogin = getUserLogin();
			Errors_Release error = new Errors_Release();
			Boolean errorVer = false;
			if (status != null && status.getName().equals("Borrador")) {
				if (release.getStatus().getId() != status.getId())
					release.setRetries(release.getRetries() + 1);
			} else if (status != null && status.getName().equals("Error")) {
				error = errorService.findById(idError);
				ReleaseError releaseError = new ReleaseError();
				releaseError.setSystem(release.getSystem());
				errorVer = true;
				releaseError.setProject(projectService.findById(release.getSystem().getProyectId()));
				releaseError.setError(error);
				Releases_WithoutObj releaseWithObj = releaseService.findReleaseWithouObj(release.getId());
				releaseError.setRelease(releaseWithObj);
				releaseError.setObservations(motive);
				Timestamp dateFormat = CommonUtils.convertStringToTimestamp(dateChange, "dd/MM/yyyy hh:mm a");
				releaseError.setErrorDate(dateFormat);
				releaseErrorService.save(releaseError);
				release.setStatus(status);
				release.setOperator(getUserLogin().getFullName());
				release.setMotive(motive);
				release.setDateChange(dateChange);
				releaseService.updateStatusRelease(release);
				release.setTimeNew(null);
				Status statusChange = statusService.findByName("Borrador");
				release.setStatus(statusChange);

				if (statusChange != null && statusChange.getName().equals("Borrador")) {
					if (release.getStatus().getId() != status.getId())
						release.setRetries(release.getRetries() + 1);
				}
				status = statusChange;
				motive = "Paso a borrador por " + error.getName();
				dateFormat = CommonUtils.convertStringToTimestamp(dateChange, "dd/MM/yyyy hh:mm a");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dateFormat);
				calendar.add(Calendar.MINUTE, 1);
				Timestamp time1Minute = new Timestamp(calendar.getTimeInMillis());
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				java.util.Date fechaNueva = (java.util.Date) format.parse(time1Minute.toString());
				format = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
				String time1MinuteFormat = format.format(fechaNueva);
				dateChange = time1MinuteFormat;
			}

			release.setStatus(status);
			release.setOperator(getUserLogin().getFullName());
			release.setDateChange(dateChange);
			release.setMotive(motive);
			releaseService.updateStatusRelease(release);
			res.setStatus("success");
			if (sendEmail) {

				if (!errorVer) {
					Integer idTemplate = Integer.parseInt(parameterService.findByCode(30).getParamValue());
					EmailTemplate emailNotify = emailService.findById(idTemplate);
					String statusName = status.getName();
					Thread newThread = new Thread(() -> {
						try {
							emailService.sendMailNotifyChangeStatus(release.getReleaseNumber(), " del Release",
									statusName, release.getOperator(),
									CommonUtils.convertStringToTimestamp(release.getDateChange(), "dd/MM/yyyy hh:mm a"),
									userLogin, senders, emailNotify, release.getMotive());
						} catch (Exception e) {
							Sentry.capture(e, "release");
						}

					});
					newThread.start();
				} else {
					Integer idTemplate = Integer.parseInt(parameterService.findByCode(31).getParamValue());
					EmailTemplate emailNotify = emailService.findById(idTemplate);
					String statusName = status.getName();
					String typeError = error.getName();
					Thread newThread = new Thread(() -> {
						try {
							emailService.sendMailNotifyChangeStatusError(typeError, release.getReleaseNumber(),
									" del Release", statusName, release.getOperator(),
									CommonUtils.convertStringToTimestamp(release.getDateChange(), "dd/MM/yyyy hh:mm a"),
									userLogin, senders, emailNotify, release.getMotive());
						} catch (Exception e) {
							Sentry.capture(e, "release");
						}

					});
					newThread.start();
				}
			}

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

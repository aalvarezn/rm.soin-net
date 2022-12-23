package com.soin.sgrm.controller.wf;

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
import com.soin.sgrm.model.ReleaseError;
import com.soin.sgrm.model.Releases_WithoutObj;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.SystemUser;
import com.soin.sgrm.model.wf.Node;
import com.soin.sgrm.model.wf.WFRelease;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.ErrorReleaseService;
import com.soin.sgrm.service.ParameterService;
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.service.ReleaseErrorService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.wf.NodeService;
import com.soin.sgrm.service.wf.WFReleaseService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.JsonSheet;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.controller.admin.ConfigurationItemController;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/manager/wf")
public class WorkFlowManagerController extends BaseController {

	public static final Logger logger = Logger.getLogger(WorkFlowManagerController.class);

	@Autowired
	private StatusService statusService;
	@Autowired
	private WFReleaseService wfReleaseService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private NodeService nodeService;
	@Autowired
	private ParameterService paramService;
	@Autowired
	private EmailTemplateService emailService;
	@Autowired
	private ErrorReleaseService errorService;
	@Autowired
	private ReleaseService releaseService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private ReleaseErrorService releaseErrorService;
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			String name = getUserLogin().getUsername();
			Object[] systemIds = systemService.myTeams(name);
			model.addAttribute("system", new SystemUser());
			model.addAttribute("systems", systemService.listSystemUserByIds(systemIds));
			model.addAttribute("status", new Status());
			model.addAttribute("statuses", statusService.list());
			model.addAttribute("errors", errorService.findAll());
			loadCountsRelease(request, systemIds);
		} catch (Exception e) {
			Sentry.capture(e, "wfReleaseManager");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina inicial/systemas." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return "/wf/workFlow/workFlowManager";

	}

	@RequestMapping(path = "/workFlowRelease", method = RequestMethod.GET)
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
			return wfReleaseService.listWorkFlowManager(name, sEcho, iDisplayStart, iDisplayLength, sSearch, null,
					dateRange, systemId, statusId, systemService.myTeams(name));
		} catch (Exception e) {
			Sentry.capture(e, "wfReleaseManager");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}
	
	@RequestMapping(value = "/wfStatus", method = RequestMethod.POST)
	public @ResponseBody JsonResponse draftRelease(HttpServletRequest request, Model model,
			@RequestParam(value = "idRelease", required = true) Integer idRelease,
			@RequestParam(value = "idNode", required = true) Integer idNode,
			@RequestParam(value = "motive", required = true) String motive,
			@RequestParam(value = "idError", required = false) Long idError
			) {
		JsonResponse res = new JsonResponse();
		try {
			WFRelease release = wfReleaseService.findWFReleaseById(idRelease);
			Node node = nodeService.findById(idNode);
			String newMotive=motive;
			node.getStatus().setMotive(newMotive);
			release.setNode(node);
			release.setStatus(node.getStatus());
			release.setOperator(getUserLogin().getFullName());
			if(node.getStatus().getName().equals("Error")) {
			Errors_Release error = errorService.findById(idError);
			ReleaseError releaseError = new ReleaseError();
			releaseError.setSystem(release.getSystem());
			releaseError.setProject(projectService.findById(release.getSystem().getProyectId()));
			releaseError.setError(error);
			Releases_WithoutObj releaseWithObj = releaseService.findReleaseWithouObj(release.getId());
			releaseError.setRelease(releaseWithObj);
			releaseError.setObservations(newMotive);
			releaseError.setErrorDate(CommonUtils.getSystemTimestamp());
			releaseErrorService.save(releaseError);
			wfReleaseService.wfStatusReleaseWithOutMin(release);
			Status statusChange = statusService.findByName("Borrador");
			release.setStatus(statusChange);
			
			if (statusChange != null && statusChange.getName().equals("Borrador")) {
				if (release.getStatus().getId() != node.getStatus().getId())
					release.setRetries(release.getRetries() + 1);
			}
			
			newMotive = "Paso a borrador por " + error.getName();
			node.getStatus().setMotive(newMotive);
			release.setNode(node);
			}
			wfReleaseService.wfStatusRelease(release);


			// Si esta marcado para enviar correo
			if (node.getSendEmail()) {
				Integer idTemplate = Integer.parseInt(paramService.findByCode(21).getParamValue());
				EmailTemplate email = emailService.findById(idTemplate);
				WFRelease releaseEmail = release;
				Thread newThread = new Thread(() -> {
					try {
						emailService.sendMail(releaseEmail, email, motive);
					} catch (Exception e) {
						Sentry.capture(e, "release");
					}
				});
				newThread.start();
			}

			// si tiene un nodo y ademas tiene actor se notifica por correo
			if (node != null && node.getActors().size() > 0) {
				Integer idTemplate = Integer.parseInt(paramService.findByCode(22).getParamValue());
				EmailTemplate emailActor = emailService.findById(idTemplate);
				WFRelease releaseEmail = release;
				Thread newThread = new Thread(() -> {
					try {
						emailService.sendMailActor(releaseEmail, emailActor);
					} catch (Exception e) {
						Sentry.capture(e, "release");
					}

				});
				newThread.start();
			}


			res.setStatus("success");
		} catch (Exception e) {
			Sentry.capture(e, "wfReleaseManagement");
			res.setStatus("exception");
			res.setException("Error al cambiar estado del release: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
/*

	@RequestMapping(value = "/wfStatus", method = RequestMethod.POST)
	public @ResponseBody JsonResponse draftRelease(HttpServletRequest request, Model model,
			@RequestParam(value = "idRelease", required = true) Integer idRelease,
			@RequestParam(value = "idNode", required = true) Integer idNode,
			@RequestParam(value = "motive", required = true) String motive) {
		JsonResponse res = new JsonResponse();
		try {
			WFRelease release = wfReleaseService.findWFReleaseById(idRelease);
			Node node = nodeService.findById(idNode);
			node.getStatus().setMotive(motive);
			release.setNode(node);
			release.setStatus(node.getStatus());
			release.setOperator(getUserLogin().getFullName());
			wfReleaseService.wfStatusRelease(release);

			// Si esta marcado para enviar correo
			if (node.getSendEmail()) {
				Integer idTemplate = Integer.parseInt(paramService.findByCode(21).getParamValue());
				EmailTemplate email = emailService.findById(idTemplate);
				WFRelease releaseEmail = release;
				Thread newThread = new Thread(() -> {
					try {
						emailService.sendMail(releaseEmail, email, motive);
					} catch (Exception e) {
						Sentry.capture(e, "release");
					}
				});
				newThread.start();
			}

			// si tiene un nodo y ademas tiene actor se notifica por correo
			if (node != null && node.getActors().size() > 0) {
				Integer idTemplate = Integer.parseInt(paramService.findByCode(22).getParamValue());
				EmailTemplate emailActor = emailService.findById(idTemplate);
				WFRelease releaseEmail = release;
				Thread newThread = new Thread(() -> {
					try {
						emailService.sendMailActor(releaseEmail, emailActor);
					} catch (Exception e) {
						Sentry.capture(e, "release");
					}

				});
				newThread.start();
			}

			res.setStatus("success");
		} catch (Exception e) {
			Sentry.capture(e, "wfReleaseManager");
			res.setStatus("exception");
			res.setException("Error al cambiar estado del release: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}*/

	public void loadCountsRelease(HttpServletRequest request, Object[] systemIds) {
		Map<String, Integer> wfCount = new HashMap<String, Integer>();
		wfCount.put("start", wfReleaseService.countByType("start", systemIds));
		wfCount.put("action", wfReleaseService.countByType("action", systemIds));
		wfCount.put("finish", wfReleaseService.countByType("finish", systemIds));
		wfCount.put("all", (wfCount.get("start") + wfCount.get("action") + wfCount.get("finish")));
		request.setAttribute("wfCount", wfCount);
	}
}

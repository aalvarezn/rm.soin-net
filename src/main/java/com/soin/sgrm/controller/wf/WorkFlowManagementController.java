package com.soin.sgrm.controller.wf;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.Errors_RFC;
import com.soin.sgrm.model.Errors_Release;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.RFCError;
import com.soin.sgrm.model.RFC_WithoutRelease;
import com.soin.sgrm.model.ReleaseError;
import com.soin.sgrm.model.ReleaseTrackingShow;
import com.soin.sgrm.model.Release_RFC;
import com.soin.sgrm.model.Release_RFCFast;
import com.soin.sgrm.model.Releases_WithoutObj;
import com.soin.sgrm.model.Request;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.StatusIncidence;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.System;
//import com.soin.sgrm.model.StatusIncidence;
import com.soin.sgrm.model.SystemUser;
import com.soin.sgrm.model.TypeRequest;
import com.soin.sgrm.model.wf.Node;
import com.soin.sgrm.model.wf.NodeIncidence;
import com.soin.sgrm.model.wf.NodeRFC;
import com.soin.sgrm.model.wf.WFIncidence;
import com.soin.sgrm.model.wf.WFRFC;
//import com.soin.sgrm.model.wf.NodeIncidence;
//import com.soin.sgrm.model.wf.WFIncidence;
import com.soin.sgrm.model.wf.WFRelease;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.ErrorRFCService;
import com.soin.sgrm.service.ErrorReleaseService;
import com.soin.sgrm.service.ParameterService;
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.service.RFCErrorService;
import com.soin.sgrm.service.RFCService;
import com.soin.sgrm.service.ReleaseErrorService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.RequestNewService;
import com.soin.sgrm.service.RequestService;
import com.soin.sgrm.service.SigesService;
import com.soin.sgrm.service.StatusIncidenceService;
import com.soin.sgrm.service.StatusRFCService;
//import com.soin.sgrm.service.StatusIncidenceService;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.TypeRequestService;
import com.soin.sgrm.service.wf.NodeService;
import com.soin.sgrm.service.wf.WFIncidenceService;
import com.soin.sgrm.service.wf.WFRFCService;
//import com.soin.sgrm.service.wf.WFIncidenceService;
import com.soin.sgrm.service.wf.WFReleaseService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.JsonSheet;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/management/wf")
public class WorkFlowManagementController extends BaseController {

	public static final Logger logger = Logger.getLogger(WorkFlowManagementController.class);

	@Autowired
	private StatusService statusService;
	
	@Autowired
	private StatusRFCService statusRFCService;
	
	@Autowired
	private WFReleaseService wfReleaseService;
	
	@Autowired
	private WFRFCService wfrfcService;

	@Autowired
	private WFIncidenceService wfIncidenceService;
	@Autowired
	private StatusIncidenceService statusIncidenceService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private NodeService nodeService;
	@Autowired
	private ParameterService paramService;
	@Autowired
	private EmailTemplateService emailService;
	@Autowired
	private ReleaseErrorService releaseErrorService;
	@Autowired
	private ErrorReleaseService errorService;
	@Autowired
	private RFCErrorService rfcErrorService;
	@Autowired
	private ErrorRFCService errorRFCService;
	@Autowired
	private ReleaseService releaseService;
	@Autowired
	private RFCService rfcService;
	@Autowired
	private SigesService sigesService;
	@Autowired
	private ProjectService projectService;

	@RequestMapping(value = "/release/", method = RequestMethod.GET)
	public String indexRelease(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			model.addAttribute("system", new SystemUser());
			model.addAttribute("systems", systemService.listSystemUser());
			model.addAttribute("status", new Status());
			model.addAttribute("statuses", statusService.list());
			model.addAttribute("errors", errorService.findAll());
		} catch (Exception e) {
			Sentry.capture(e, "wfReleaseManagement");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina inicial/systemas." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return "/wf/workFlow/workFlowManagementRelease";

	}

	
	@RequestMapping(value = "/rfc/", method = RequestMethod.GET)
	public String indexRFC(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			model.addAttribute("system", new SystemUser());
			model.addAttribute("systems", systemService.listSystemUser());
			model.addAttribute("status", new Status());
			model.addAttribute("statuses", statusRFCService.findAll());
			model.addAttribute("errors", errorService.findAll());
		} catch (Exception e) {
			Sentry.capture(e, "wfReleaseManagement");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina inicial/systemas." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return "/wf/workFlow/workFlowManagementRFC";

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
			return wfReleaseService.listWorkFlowRelease(name, sEcho, iDisplayStart, iDisplayLength, sSearch, null,
					dateRange, systemId, statusId);
		} catch (Exception e) {
			Sentry.capture(e, "wfReleaseManagement");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}
	
	@RequestMapping(path = "/workFlowRFC", method = RequestMethod.GET)
	public @ResponseBody JsonSheet<?> getSystemRFC(HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
		try {
			String range = request.getParameter("dateRange");
			String[] dateRange = (range != null) ? range.split("-") : null;
			Integer systemId = Integer.parseInt(request.getParameter("systemId"));
			Long statusId = (long) Integer.parseInt(request.getParameter("statusId"));

			String name = getUserLogin().getUsername(), sSearch = request.getParameter("sSearch");
			int sEcho = Integer.parseInt(request.getParameter("sEcho")),
					iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart")),
					iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			return wfrfcService.listWorkFlowManager(name, sEcho, iDisplayStart, iDisplayLength, sSearch, null,
					dateRange, systemId, statusId, systemService.myTeams(name));
		} catch (Exception e) {
			Sentry.capture(e, "wfRFCManager");
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
						String motiveNow="";
						if(node.getStatus().getMotive()==null) {
							motiveNow=node.getStatus().getDescription();
						}else {
							motiveNow=node.getStatus().getMotive();
						}
						emailService.sendMail(releaseEmail, email, motiveNow);
					} catch (Exception e) {
						Sentry.capture(e, "release");
					}
				});
				newThread.start();
			}
			
			// Si esta marcado para enviar correo
			if (node.getUsers().size()>0) {
				Integer idTemplate = Integer.parseInt(paramService.findByCode(23).getParamValue());

				EmailTemplate emailNotify = emailService.findById(idTemplate);
				
				String user = getUserLogin().getFullName();
				Thread newThread = new Thread(() -> {
					try {
						emailService.sendMailNotify(release, emailNotify, user);
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
	@RequestMapping(value = "/wfStatusRFC", method = RequestMethod.POST)
	public @ResponseBody JsonResponse draftRFC(HttpServletRequest request, Model model,
			@RequestParam(value = "idRFC", required = true) Long idRFC,
			@RequestParam(value = "idNode", required = true) Integer idNode,
			@RequestParam(value = "motive", required = true) String motive,
			@RequestParam(value = "idError", required = false) Long idError
			) {
		JsonResponse res = new JsonResponse();
		String newMotive=motive;
		try {
			WFRFC rfc = wfrfcService.findWFRFCById(idRFC);
			NodeRFC node = nodeService.findByIdNoRFC(idNode);
			
			node.getStatus().setReason(newMotive);

			rfc.setNode(node);
			rfc.setStatus(node.getStatus());
			
			String user = getUserLogin().getFullName();
			rfc.setOperator(user);
			if (node.getStatus() != null && node.getStatus().getName().equals("Borrador")) {
				Set<Release_RFCFast> releases = rfc.getReleases();
				for (Release_RFCFast release : releases) {
					release.setStatus(release.getStatusBefore());
					release.setMotive("Devuelto al estado " + release.getStatus().getName());
					releaseService.updateStatusReleaseRFC(release, user);
				} 

				/*
				 * if (release.getStatus().getId() != status.getId())
				 * release.setRetries(release.getRetries() + 1);
				 */
			}else if (node.getStatus() != null && node.getStatus().getName().equals("Error")) {
				Errors_RFC error = errorRFCService.findById(idError);
				RFCError rfcError = new RFCError();
				rfcError.setSystem(rfc.getSystemInfo());
				rfcError.setSiges(sigesService.findByKey("codeSiges", rfc.getCodeProyect()));
				rfcError.setError(error);
				RFC_WithoutRelease rfcWithRelease = rfcService.findRfcWithRelease(rfc.getId());
				rfcError.setRfc(rfcWithRelease);
				rfcError.setObservations(newMotive);
				Timestamp dateFormat = CommonUtils.getSystemTimestamp();
				rfcError.setErrorDate(dateFormat);
				node.getStatus().setReason(newMotive);
				rfc.setNode(node);
				rfc.setStatus(node.getStatus());
				rfc.setOperator(getUserLogin().getFullName());
				rfcErrorService.save(rfcError);
				rfc.setOperator(getUserLogin().getFullName());
				rfc.setMotive(motive);
				rfc.setRequestDate(dateFormat);

				wfrfcService.wfStatusRFCWithOutMin(rfc);
				StatusRFC statusChange = statusRFCService.findByKey("name","Borrador");
				rfc.setStatus(statusChange);

				if (statusChange != null && statusChange.getName().equals("Borrador")) {
					Set<Release_RFCFast> releases = rfc.getReleases();
					for (Release_RFCFast release : releases) {
						release.setStatus(release.getStatusBefore());
						release.setMotive("Devuelto al estado " + release.getStatus().getName());
						releaseService.updateStatusReleaseRFC(release, user);
					} 
				}
				newMotive = "Paso a borrador por " + error.getName();
				
				node.getStatus().setReason(newMotive);
				rfc.setNode(node);
			}
			wfrfcService.wfStatusRFC(rfc);


			// Si esta marcado para enviar correo
			if (node.getSendEmail()) {
				Integer idTemplate = Integer.parseInt(paramService.findByCode(29).getParamValue());
				EmailTemplate email = emailService.findById(idTemplate);
				WFRFC rfcEmail = rfc;
				Thread newThread = new Thread(() -> {
					try {
						emailService.sendMailRFC(rfcEmail, email, motive);
					} catch (Exception e) {
						Sentry.capture(e, "release");
					}
				});
				newThread.start();
			}

			// si tiene un nodo y ademas tiene actor se notifica por correo
			if (node != null && node.getActors().size() > 0) {
				Integer idTemplate = Integer.parseInt(paramService.findByCode(27).getParamValue());
				EmailTemplate emailActor = emailService.findById(idTemplate);
				WFRFC rfcEmail = rfc;
				Thread newThread = new Thread(() -> {
					try {
						emailService.sendMailActorRFC(rfcEmail, emailActor);
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

	@RequestMapping(path = "/workFlowIncidence", method = RequestMethod.GET)
	public @ResponseBody JsonSheet<?> getIncidences(HttpServletRequest request, Locale locale, Model model,
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
			return wfReleaseService.listWorkFlowRelease(name, sEcho, iDisplayStart, iDisplayLength, sSearch, null,
					dateRange, systemId, statusId);
		} catch (Exception e) {
			Sentry.capture(e, "wfReleaseManagement");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}
	@RequestMapping(value = "/wfStatusIncidence", method = RequestMethod.POST)
	public @ResponseBody JsonResponse draftIncidence(HttpServletRequest request, Model model,
			@RequestParam(value = "idIncidence", required = true) Long idIncidence,
			@RequestParam(value = "idNode", required = true) Integer idNode,
			@RequestParam(value = "motive", required = true) String motive) {
		JsonResponse res = new JsonResponse();
		try {
			WFIncidence incidence = wfIncidenceService.findWFIncidenceById(idIncidence);
			NodeIncidence node = nodeService.findByIdNoInci(idNode);
			node.getStatus().getStatus().setReason(motive);
			incidence.setNode(node);
			incidence.setStatus(node.getStatus());
			incidence.setOperator(getUserLogin().getFullName());
			wfIncidenceService.wfStatusIncidence(incidence);

			// Si esta marcado para enviar correo
			if (node.getSendEmail()) {
				Integer idTemplate = Integer.parseInt(paramService.findByCode(24).getParamValue());
				EmailTemplate email = emailService.findById(idTemplate);
				WFIncidence incidenceEmail = incidence;
				Thread newThread = new Thread(() -> {
					try {
						emailService.sendMailIncidence(incidenceEmail, email, motive);
					} catch (Exception e) {
						Sentry.capture(e, "release");
					}
				});
				newThread.start();
			}

			res.setStatus("success");
		} catch (Exception e) {
			Sentry.capture(e, "wfIncidenceManagement");
			res.setStatus("exception");
			res.setException("Error al cambiar estado del release: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	@RequestMapping(value = "/incidence", method = RequestMethod.GET)
	public String indexIncidence(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			model.addAttribute("system", new SystemUser());
			model.addAttribute("systems", systemService.listSystemUser());
			model.addAttribute("status", new StatusIncidence());
			model.addAttribute("statuses", statusIncidenceService.findAll());
		} catch (Exception e) {
			Sentry.capture(e, "wfReleaseManagement");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina inicial/systemas." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return "/wf/workFlow/workFlowManagementIncidence";

	}
	@RequestMapping(value = "/nodesearch/{idNode}", method = RequestMethod.GET)
	public @ResponseBody JsonResponse nodeSearch(@PathVariable int idNode, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			Node node = nodeService.findById(idNode);
			res.setStatus("success");
			res.setObj(node);
		} catch (Exception e) {
			Sentry.capture(e, "node");
			res.setStatus("exception");
			res.setException("Error al procesar consulta: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
/*
	@RequestMapping(value = "/wfStatusIncidence", method = RequestMethod.POST)
	public @ResponseBody JsonResponse draftIncidence(HttpServletRequest request, Model model,
			@RequestParam(value = "idIncidence", required = true) Long idIncidence,
			@RequestParam(value = "idNode", required = true) Integer idNode,
			@RequestParam(value = "motive", required = true) String motive) {
		JsonResponse res = new JsonResponse();
		try {
			WFIncidence incidence = wfIncidenceService.findWFIncidenceById(idIncidence);
			NodeIncidence node = nodeService.findByIdNoInci(idNode);
			node.getStatus().setReason(motive);
			incidence.setNode(node);
			incidence.setStatus(node.getStatus());
			incidence.setOperator(getUserLogin().getFullName());
			wfIncidenceService.wfStatusIncidence(incidence);

			// Si esta marcado para enviar correo
			if (node.getSendEmail()) {
				Integer idTemplate = Integer.parseInt(paramService.findByCode(24).getParamValue());
				EmailTemplate email = emailService.findById(idTemplate);
				WFIncidence incidenceEmail = incidence;
				Thread newThread = new Thread(() -> {
					try {
						emailService.sendMailIncidence(incidenceEmail, email, motive);
					} catch (Exception e) {
						Sentry.capture(e, "release");
					}
				});
				newThread.start();
			}

			res.setStatus("success");
		} catch (Exception e) {
			Sentry.capture(e, "wfIncidenceManagement");
			res.setStatus("exception");
			res.setException("Error al cambiar estado del release: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	@RequestMapping(value = "/incidence", method = RequestMethod.GET)
	public String indexIncidence(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			model.addAttribute("system", new SystemUser());
			model.addAttribute("systems", systemService.listSystemUser());
			model.addAttribute("status", new StatusIncidence());
			model.addAttribute("statuses", statusIncidenceService.findAll());
		} catch (Exception e) {
			Sentry.capture(e, "wfReleaseManagement");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina inicial/systemas." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return "/wf/workFlow/workFlowManagementIncidence";

	}
 */
	
}
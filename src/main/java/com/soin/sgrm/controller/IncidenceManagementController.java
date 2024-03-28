package com.soin.sgrm.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.AttentionGroup;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.SystemTypeIncidence;
import com.soin.sgrm.model.System_Priority;
import com.soin.sgrm.model.System_StatusIn;
import com.soin.sgrm.model.User;
import com.soin.sgrm.model.pos.PAttentionGroup;
import com.soin.sgrm.model.pos.PEmailTemplate;
import com.soin.sgrm.model.pos.PIncidence;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PSystemTypeIncidence;
import com.soin.sgrm.model.pos.PSystem_Priority;
import com.soin.sgrm.model.pos.PSystem_StatusIn;
import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.model.pos.wf.PNodeIncidence;
import com.soin.sgrm.model.pos.wf.PWFIncidence;
import com.soin.sgrm.model.wf.NodeIncidence;
import com.soin.sgrm.model.wf.WFIncidence;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.AttentionGroupService;
import com.soin.sgrm.service.EmailReadService;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.IncidenceService;
import com.soin.sgrm.service.ParameterService;
import com.soin.sgrm.service.RFCService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.SigesService;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.SystemTypeIncidenceService;
import com.soin.sgrm.service.System_PriorityService;
import com.soin.sgrm.service.System_StatusInService;
import com.soin.sgrm.service.TreeService;
import com.soin.sgrm.service.TypeChangeService;
import com.soin.sgrm.service.pos.PAttentionGroupService;
import com.soin.sgrm.service.pos.PEmailTemplateService;
import com.soin.sgrm.service.pos.PIncidenceService;
import com.soin.sgrm.service.pos.PParameterService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.service.pos.PSystemTypeIncidenceService;
import com.soin.sgrm.service.pos.PSystem_PriorityService;
import com.soin.sgrm.service.pos.PSystem_StatusInService;
import com.soin.sgrm.service.pos.PTypeChangeService;
import com.soin.sgrm.service.pos.PUserService;
import com.soin.sgrm.service.pos.wf.PNodeService;
import com.soin.sgrm.service.wf.NodeService;
import com.soin.sgrm.service.wf.WFIncidenceService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/incidenceManagement")
public class IncidenceManagementController extends BaseController {


	@Autowired
	System_StatusInService statusService;

	@Autowired
	SystemService systemService;

	@Autowired
	TypeChangeService typeChangeService;

	@Autowired
	ParameterService parameterService;

	@Autowired
	EmailTemplateService emailService;

	@Autowired
	SystemTypeIncidenceService typeIncidenceService;

	@Autowired
	IncidenceService incidenceService;

	@Autowired
	System_PriorityService priorityIncidenceService;

	@Autowired
	com.soin.sgrm.service.UserService userService;

	@Autowired
	AttentionGroupService attentionGroupService;

	@Autowired
	NodeService nodeService;

	@Autowired
	PSystem_StatusInService pstatusService;

	@Autowired
	PSystemService psystemService;

	@Autowired
	PTypeChangeService ptypeChangeService;

	@Autowired
	PParameterService pparameterService;

	@Autowired
	PEmailTemplateService pemailService;

	@Autowired
	PSystemTypeIncidenceService ptypeIncidenceService;

	@Autowired
	PIncidenceService pincidenceService;

	@Autowired
	PSystem_PriorityService ppriorityIncidenceService;

	@Autowired
	PUserService puserService;

	@Autowired
	PAttentionGroupService pattentionGroupService;

	@Autowired
	PNodeService pnodeService;
	public static final Logger logger = Logger.getLogger(IncidenceManagementController.class);
	private final Environment environment;

	@Autowired
	public IncidenceManagementController(Environment environment) {
		this.environment = environment;
	}

	public String profileActive() {
		String[] activeProfiles = environment.getActiveProfiles();
		for (String profile : activeProfiles) {
			return profile;
		}
		return "";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			Integer userLogin = getUserLogin().getId();
			loadCountsRelease(request, userLogin);
			if (profileActive().equals("oracle")) {

				List<AttentionGroup> attentionGroups = attentionGroupService.findGroupByUserId(userLogin);
				List<Long> listAttentionGroupId = new ArrayList<Long>();
				for (AttentionGroup attentionGroup : attentionGroups) {
					listAttentionGroupId.add(attentionGroup.getId());
				}
				List<System> systemList = systemService.findByGroupIncidence(listAttentionGroupId);
				Set<System> systemWithRepeat = new LinkedHashSet<>(systemList);
				systemList.clear();
				systemList.addAll(systemWithRepeat);

				List<SystemTypeIncidence> typeIncidences = typeIncidenceService.findAll();
				List<System_StatusIn> statuses = statusService.findAll();
				List<System_Priority> priorities = priorityIncidenceService.findAll();
				model.addAttribute("typeincidences", typeIncidences);
				model.addAttribute("priorities", priorities);
				model.addAttribute("statuses", statuses);
				model.addAttribute("systems", systemList);
			} else if (profileActive().equals("postgres")) {

				List<PAttentionGroup> attentionGroups = pattentionGroupService.findGroupByUserId(userLogin);
				List<Long> listAttentionGroupId = new ArrayList<Long>();
				for (PAttentionGroup attentionGroup : attentionGroups) {
					listAttentionGroupId.add(attentionGroup.getId());
				}
				List<PSystem> systemList = psystemService.findByGroupIncidence(listAttentionGroupId);
				Set<PSystem> systemWithRepeat = new LinkedHashSet<>(systemList);
				systemList.clear();
				systemList.addAll(systemWithRepeat);

				List<PSystemTypeIncidence> typeIncidences = ptypeIncidenceService.findAll();
				List<PSystem_StatusIn> statuses = pstatusService.findAll();
				List<PSystem_Priority> priorities = ppriorityIncidenceService.findAll();
				model.addAttribute("typeincidences", typeIncidences);
				model.addAttribute("priorities", priorities);
				model.addAttribute("statuses", statuses);
				model.addAttribute("systems", systemList);
			}

		} catch (Exception e) {
			Sentry.capture(e, "incidence");
			e.printStackTrace();
		}
		return "/incidence/incidenceManagement";

	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		
		try {

			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			Integer userLogin = getUserLogin().getId();
			Long statusId;
			Long priorityId;
			Integer systemId;
			Long typeId;
			String sSearch = request.getParameter("sSearch");
			if (request.getParameter("systemId").equals("")) {
				systemId = 0;
			} else {

				systemId = Integer.parseInt(request.getParameter("systemId"));

			}
			if (request.getParameter("statusId").equals("")) {
				statusId = (long) 0;
			} else {
				statusId = (long) Integer.parseInt(request.getParameter("statusId"));
			}
			if (request.getParameter("priorityId").equals("")) {
				priorityId = (long) 0;
			} else {
				priorityId = (long) Integer.parseInt(request.getParameter("priorityId"));
			}

			if (request.getParameter("typeId").equals("")) {
				typeId = (long) 0;
			} else {
				typeId = (long) Integer.parseInt(request.getParameter("typeId"));
			}
			String dateRange = request.getParameter("dateRange");
			if (profileActive().equals("oracle")) {
				JsonSheet<Incidence> incidences = new JsonSheet<>();
				incidences = incidenceService.findAllRequest2(dateRange, sEcho, iDisplayStart, iDisplayLength, sSearch,
						statusId, dateRange, typeId, priorityId, systemId, userLogin);
				return incidences;
			} else if (profileActive().equals("postgres")) {
				JsonSheet<PIncidence> incidences = new JsonSheet<>();
				incidences = pincidenceService.findAllRequest2(dateRange, sEcho, iDisplayStart, iDisplayLength, sSearch,
						statusId, dateRange, typeId, priorityId, systemId, userLogin);
				return incidences;
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}

	@RequestMapping(value = "/editIncidence-{id}", method = RequestMethod.GET)
	public String editIncidence(@PathVariable String id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		
		try {
			if (profileActive().equals("oracle")) {
				Incidence incidenceEdit = new Incidence();
				User user = userService.getUserByUsername(getUserLogin().getUsername());
				List<System> systems = systemService.listProjects(user.getId());
				model.addAttribute("parameter", id);
				model.addAttribute("systems", systems);
				model.addAttribute("typeChange", typeChangeService.findAll());
				model.addAttribute("priorities", priorityIncidenceService.findAll());
				model.addAttribute("typeincidences", typeIncidenceService.findAll());
				model.addAttribute("incidence", incidenceEdit);
				model.addAttribute("senders", incidenceEdit.getSenders());
				model.addAttribute("message", incidenceEdit.getMessage());

				if (CommonUtils.isNumeric(id)) {
					incidenceEdit = incidenceService.getIncidences((Long.parseLong(id)));
				}

				if (incidenceEdit == null) {
					return "redirect:/";
				}

				model.addAttribute("incidence", incidenceEdit);
			} else if (profileActive().equals("postgres")) {
				PIncidence incidenceEdit = new PIncidence();
				PUser user = puserService.getUserByUsername(getUserLogin().getUsername());
				List<PSystem> systems = psystemService.listProjects(user.getId());
				model.addAttribute("parameter", id);
				model.addAttribute("systems", systems);
				model.addAttribute("typeChange", ptypeChangeService.findAll());
				model.addAttribute("priorities", ppriorityIncidenceService.findAll());
				model.addAttribute("typeincidences", ptypeIncidenceService.findAll());
				model.addAttribute("incidence", incidenceEdit);
				model.addAttribute("senders", incidenceEdit.getSenders());
				model.addAttribute("message", incidenceEdit.getMessage());

				if (CommonUtils.isNumeric(id)) {
					incidenceEdit = pincidenceService.getIncidences((Long.parseLong(id)));
				}

				if (incidenceEdit == null) {
					return "redirect:/";
				}

				model.addAttribute("incidence", incidenceEdit);
			}
			
			return "/incidence/summaryIncidence";

		} catch (Exception e) {
			Sentry.capture(e, "incidence");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina resumen incidence." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/homeIncidenceAttention";
		}

	}

	@RequestMapping(value = "/assignIncidence", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse createRelease(HttpServletRequest request,
			@RequestBody Incidence incidenceUserChange) {
		// Se genera la estructura base del release para su posterior creacion completa.
		JsonResponse res = new JsonResponse();

		try {
			res.setStatus("success");
			res.setMessage("Se asigno correctamente el ticket al usuario!");
			if (profileActive().equals("oracle")) {
				User user = userService.getUserByUsername(getUserLogin().getUsername());
				User newUser = userService.findUserById(incidenceUserChange.getUserNewId());
				Incidence incidence = incidenceService.findById(incidenceUserChange.getTicketId());
				incidence.setMotive(incidenceUserChange.getMotive());
				incidence.setUser(newUser);
				incidence.setOperator(user.getFullName());
				incidence.setAssigned(newUser.getFullName());
				incidence.setUpdateDate(CommonUtils.getSystemTimestamp());
				incidenceService.update(incidence);

				Integer idTemplate = Integer.parseInt(parameterService.findByCode(32).getParamValue());
				EmailTemplate emailNotify = emailService.findById(idTemplate);
				Thread newThread = new Thread(() -> {
					try {
						emailService.sendMailNotifyChangeUserIncidence(incidence.getNumTicket(), user,
								incidence.getMotive(), incidence.getUpdateDate(), newUser, emailNotify);
					} catch (Exception e) {
						Sentry.capture(e, "incidence");
					}

				});
				newThread.start();
			} else if (profileActive().equals("postgres")) {
				PUser user = puserService.getUserByUsername(getUserLogin().getUsername());
				PUser newUser = puserService.findUserById(incidenceUserChange.getUserNewId());
				PIncidence incidence = pincidenceService.findById(incidenceUserChange.getTicketId());
				incidence.setMotive(incidenceUserChange.getMotive());
				incidence.setUser(newUser);
				incidence.setOperator(user.getFullName());
				incidence.setAssigned(newUser.getFullName());
				incidence.setUpdateDate(CommonUtils.getSystemTimestamp());
				pincidenceService.update(incidence);

				Integer idTemplate = Integer.parseInt(parameterService.findByCode(32).getParamValue());
				PEmailTemplate emailNotify = pemailService.findById(idTemplate);
				Thread newThread = new Thread(() -> {
					try {
						pemailService.sendMailNotifyChangeUserIncidence(incidence.getNumTicket(), user,
								incidence.getMotive(), incidence.getUpdateDate(), newUser, emailNotify);
					} catch (Exception e) {
						Sentry.capture(e, "incidence");
					}

				});
				newThread.start();
			}
			

		} catch (Exception e) {
			Sentry.capture(e, "incidenceChangeUser");
			res.setStatus("error");
			res.setException("La acción no se pudo completar correctamente.");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/attentionIncidence-{status}", method = RequestMethod.GET)
	public String summmary(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {

		try {
			model.addAttribute("parameter", status);
			
			if (profileActive().equals("oracle")) {
				Incidence incidence = null;
				if (CommonUtils.isNumeric(status)) {
					incidence = incidenceService.getIncidences((Long.parseLong(status)));
				}

				if (incidence == null) {
					return "redirect:/";
				}
				String userName = getUserLogin().getUsername();
				if (!incidence.getUser().getUsername().equals(userName)) {
					redirectAttributes.addFlashAttribute("data", "Ticket no disponible para este usuario ");
					return "redirect:/homeIncidenceAttention";
				}

				model.addAttribute("incidence", incidence);
			} else if (profileActive().equals("postgres")) {
				PIncidence incidence = null;
				if (CommonUtils.isNumeric(status)) {
					incidence = pincidenceService.getIncidences((Long.parseLong(status)));
				}

				if (incidence == null) {
					return "redirect:/";
				}
				String userName = getUserLogin().getUsername();
				if (!incidence.getUser().getUsername().equals(userName)) {
					redirectAttributes.addFlashAttribute("data", "Ticket no disponible para este usuario ");
					return "redirect:/homeIncidenceAttention";
				}

				model.addAttribute("incidence", incidence);
			}
			

		} catch (Exception e) {
			Sentry.capture(e, "incidence");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina ticket." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/homeIncidenceAttention";
		}

		return "/incidence/incidenceAttention";
	}

	@RequestMapping(value = "/summaryIncidenceAttention-{status}", method = RequestMethod.GET)
	public String summmaryIncidenceAttention(@PathVariable String status, HttpServletRequest request, Locale locale,
			Model model, HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {
		try {
			model.addAttribute("parameter", status);
			if (profileActive().equals("oracle")) {
				Incidence incidence = null;
				if (CommonUtils.isNumeric(status)) {
					incidence = incidenceService.getIncidences((Long.parseLong(status)));
				}

				if (incidence == null) {
					return "redirect:/";
				}

				model.addAttribute("incidence", incidence);
			} else if (profileActive().equals("postgres")) {
				PIncidence incidence = null;
				if (CommonUtils.isNumeric(status)) {
					incidence = pincidenceService.getIncidences((Long.parseLong(status)));
				}

				if (incidence == null) {
					return "redirect:/";
				}

				model.addAttribute("incidence", incidence);
			}
			

		} catch (Exception e) {
			Sentry.capture(e, "incidence");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina resumen incidence." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/homeIncidenceAttention";
		}

		return "/incidence/summaryIncidenceAttention";
	}

	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public @ResponseBody JsonResponse draftRelease(HttpServletRequest request, Model model,
			@RequestParam(value = "idIncidence", required = true) Long idIncidence,
			@RequestParam(value = "idNode", required = true) Integer idNode,

			@RequestParam(value = "motive", required = true) String motive,
			@RequestParam(value = "cause", required = false) String cause,
			@RequestParam(value = "errorNew", required = false) String errorNew,
			@RequestParam(value = "solution", required = false) String solution
			) {

		JsonResponse res = new JsonResponse();
		try {
			
			if (profileActive().equals("oracle")) {
				User user = userService.getUserByUsername(getUserLogin().getUsername());
				Incidence incidence = incidenceService.findById(idIncidence);
				NodeIncidence node = nodeService.findByIdNoInci(idNode);

				
				if(cause!=null) {
					incidence.setCause(cause);
					incidence.setSolution(solution);
					incidence.setErrorNew(errorNew);
				}

				// node.getStatus().setReason(motive);
				incidence.setNode(node);
				incidence.setStatus(node.getStatus());
				incidence.setSlaActive(node.getStatus().getSlaActive());
				incidence.setOperator(getUserLogin().getFullName());
				incidence.setMotive(motive);
				if (node.getStatus() != null) {

					if (node.getActors() != null) {
						for (AttentionGroup attentionGroup : node.getActors()) {
							incidence.setUser(attentionGroup.getLead());
							incidence.setAssigned(attentionGroup.getLead().getFullName());
						}
					}
					incidence.setSlaActive(node.getStatus().getSlaActive());
				}

				incidence.setOperator(user.getFullName());
				incidence.setUser(user);
				incidence.setUpdateDate(CommonUtils.getSystemTimestamp());
				incidenceService.update(incidence);

				if (node.getSendEmail()) {
					if (node != null && node.getActors().size() > 0) {
						Integer idTemplate = Integer.parseInt(parameterService.findByCode(25).getParamValue());
						EmailTemplate emailActor = emailService.findById(idTemplate);
						WFIncidence incidenceEmail = new WFIncidence();
						incidenceEmail.convertReleaseToWFIncidence(incidence);
						Thread newThread = new Thread(() -> {
							try {
								emailService.sendMailActorIncidence(incidenceEmail, emailActor);
							} catch (Exception e) {
								Sentry.capture(e, "release");
							}

						});
						newThread.start();
					}

					// si tiene un nodo y ademas tiene actor se notifica por correo
					if (node != null && node.getUsers().size() > 0) {
						Integer idTemplate = Integer.parseInt(parameterService.findByCode(26).getParamValue());

						EmailTemplate emailNotify = emailService.findById(idTemplate);
						WFIncidence incidenceEmail = new WFIncidence();
						incidenceEmail.convertReleaseToWFIncidence(incidence);
						String userS = getUserLogin().getFullName();
						Thread newThread = new Thread(() -> {
							try {
								emailService.sendMailNotify(incidenceEmail, emailNotify, userS);
							} catch (Exception e) {
								Sentry.capture(e, "release");
							}

						});
						newThread.start();
					}
				}
			} else if (profileActive().equals("postgres")) {
				PUser user = puserService.getUserByUsername(getUserLogin().getUsername());
				PIncidence incidence = pincidenceService.findById(idIncidence);
				PNodeIncidence node = pnodeService.findByIdNoInci(idNode);

				
				if(cause!=null) {
					incidence.setCause(cause);
					incidence.setSolution(solution);
					incidence.setErrorNew(errorNew);
				}

				// node.getStatus().setReason(motive);
				incidence.setNode(node);
				incidence.setStatus(node.getStatus());
				incidence.setSlaActive(node.getStatus().getSlaActive());
				incidence.setOperator(getUserLogin().getFullName());
				incidence.setMotive(motive);
				if (node.getStatus() != null) {

					if (node.getActors() != null) {
						for (PAttentionGroup attentionGroup : node.getActors()) {
							incidence.setUser(attentionGroup.getLead());
							incidence.setAssigned(attentionGroup.getLead().getFullName());
						}
					}
					incidence.setSlaActive(node.getStatus().getSlaActive());
				}

				incidence.setOperator(user.getFullName());
				incidence.setUser(user);
				incidence.setUpdateDate(CommonUtils.getSystemTimestamp());
				pincidenceService.update(incidence);

				if (node.getSendEmail()) {
					if (node != null && node.getActors().size() > 0) {
						Integer idTemplate = Integer.parseInt(parameterService.findByCode(25).getParamValue());
						PEmailTemplate emailActor = pemailService.findById(idTemplate);
						PWFIncidence incidenceEmail = new PWFIncidence();
						incidenceEmail.convertReleaseToWFIncidence(incidence);
						Thread newThread = new Thread(() -> {
							try {
								pemailService.sendMailActorIncidence(incidenceEmail, emailActor);
							} catch (Exception e) {
								Sentry.capture(e, "release");
							}

						});
						newThread.start();
					}

					// si tiene un nodo y ademas tiene actor se notifica por correo
					if (node != null && node.getUsers().size() > 0) {
						Integer idTemplate = Integer.parseInt(parameterService.findByCode(26).getParamValue());

						PEmailTemplate emailNotify = pemailService.findById(idTemplate);
						PWFIncidence incidenceEmail = new PWFIncidence();
						incidenceEmail.convertReleaseToWFIncidence(incidence);
						String userS = getUserLogin().getFullName();
						Thread newThread = new Thread(() -> {
							try {
								pemailService.sendMailNotify(incidenceEmail, emailNotify, userS);
							} catch (Exception e) {
								Sentry.capture(e, "release");
							}

						});
						newThread.start();
					}
				}
			}
			
			res.setMessage("Se tramito correctamente!");
			res.setStatus("success");
		} catch (Exception e) {
			Sentry.capture(e, "wfReleaseManagement");
			res.setStatus("exception");
			res.setException("Error al cambiar estado del release: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return res;
	}

	public List<String> getCC(String ccs) {

		List<String> getCC = new ArrayList<String>();
		if (ccs != null) {
			ccs.split(",");
			for (String cc : ccs.split(",")) {
				getCC.add(cc);
			}
		}
		return getCC;
	}

	public void loadCountsRelease(HttpServletRequest request, Integer id) {

		Integer userLogin = getUserLogin().getId();
		String email = getUserLogin().getFullName();

		Map<String, Integer> userC = new HashMap<String, Integer>();
		if (profileActive().equals("oracle")) {
			userC.put("draft", incidenceService.countByType(id, "Borrador", 1, null, userLogin, email));
			userC.put("requested", incidenceService.countByType(id, "Solicitado", 1, null, userLogin, email));
			userC.put("completed", incidenceService.countByType(id, "Completado", 1, null, userLogin, email));
			userC.put("all", (userC.get("draft") + userC.get("requested") + userC.get("completed")));
			request.setAttribute("userC", userC);
		} else if (profileActive().equals("postgres")) {
			userC.put("draft", pincidenceService.countByType(id, "Borrador", 1, null, userLogin, email));
			userC.put("requested", pincidenceService.countByType(id, "Solicitado", 1, null, userLogin, email));
			userC.put("completed", pincidenceService.countByType(id, "Completado", 1, null, userLogin, email));
			userC.put("all", (userC.get("draft") + userC.get("requested") + userC.get("completed")));
			request.setAttribute("userC", userC);
		}
		
	}

}
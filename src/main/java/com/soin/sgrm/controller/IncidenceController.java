package com.soin.sgrm.controller;

import java.awt.print.Printable;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Sets;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.AttentionGroup;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.PriorityIncidence;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.Release_RFC;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.StatusIncidence;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.SystemTypeIncidence;
import com.soin.sgrm.model.System_Priority;
import com.soin.sgrm.model.System_StatusIn;
import com.soin.sgrm.model.TypeChange;
import com.soin.sgrm.model.TypeIncidence;
import com.soin.sgrm.model.User;
import com.soin.sgrm.model.wf.Node;
import com.soin.sgrm.model.wf.NodeIncidence;
import com.soin.sgrm.model.wf.WFIncidence;
import com.soin.sgrm.model.wf.WFRelease;
import com.soin.sgrm.model.wf.WFUser;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.AttentionGroupService;
import com.soin.sgrm.service.EmailReadService;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.IncidenceService;
import com.soin.sgrm.service.ParameterService;
import com.soin.sgrm.service.PriorityIncidenceService;
import com.soin.sgrm.service.RFCService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.SigesService;
import com.soin.sgrm.service.StatusIncidenceService;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.SystemTypeIncidenceService;
import com.soin.sgrm.service.System_PriorityService;
import com.soin.sgrm.service.System_StatusInService;
import com.soin.sgrm.service.TreeService;
import com.soin.sgrm.service.TypeChangeService;
import com.soin.sgrm.service.TypeIncidenceService;
import com.soin.sgrm.service.wf.NodeService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyError;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping(value = "/incidence")
public class IncidenceController extends BaseController {
	@Autowired
	RFCService rfcService;
	@Autowired
	System_StatusInService statusService;
	@Autowired
	StatusService statusReleaseService;
	@Autowired
	SystemService systemService;
	@Autowired
	SigesService sigeService;
	@Autowired
	TypeChangeService typeChangeService;
	@Autowired
	ReleaseService releaseService;
	@Autowired
	ParameterService parameterService;
	@Autowired
	EmailTemplateService emailService;
	@Autowired
	TreeService treeService;
	@Autowired
	SystemTypeIncidenceService typeIncidenceService;
	@Autowired
	IncidenceService incidenceService;
	@Autowired
	System_PriorityService priorityIncidenceService;
	@Autowired
	EmailReadService emailReadService;
	@Autowired
	AttentionGroupService attentionGroupService;
	@Autowired
	com.soin.sgrm.service.UserService userService;
	@Autowired
	NodeService nodeService;
	@Autowired
	ParameterService paramService;
	public static final Logger logger = Logger.getLogger(IncidenceController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			Integer userLogin = getUserLogin().getId();
			loadCountsRelease(request, userLogin);
			List<System> systems = systemService.findByUserIncidence(userLogin);
			List<SystemTypeIncidence> typeIncidences = typeIncidenceService.findAll();
			List<System_StatusIn> statuses = statusService.findAll();
			List<System_Priority> priorities = priorityIncidenceService.findAll();
			model.addAttribute("typeincidences", typeIncidences);
			model.addAttribute("priorities", priorities);
			model.addAttribute("statuses", statuses);
			model.addAttribute("systems", systems);
		} catch (Exception e) {
			Sentry.capture(e, "incidence");
			e.printStackTrace();
		}
		return "/incidence/incidence";

	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<Incidence> incidences = new JsonSheet<>();
		try {

			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			String email = getUserLogin().getFullName();
			String sSearch = request.getParameter("sSearch");
			Long statusId;
			Long priorityId;
			Integer systemId;
			Integer userLogin = getUserLogin().getId();
			Long typeId;
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

			incidences = incidenceService.findAllRequest(email, sEcho, iDisplayStart, iDisplayLength, sSearch, statusId,
					dateRange, typeId, priorityId, userLogin, systemId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return incidences;
	}

	@RequestMapping(value = "/editIncidence-{id}", method = RequestMethod.GET)
	public String editIncidence(@PathVariable Long id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		Incidence incidenceEdit = new Incidence();
		User user = userService.getUserByUsername(getUserLogin().getUsername());
		List<System> systems = systemService.listProjects(user.getId());
		try {
			if (id == null) {
				return "redirect:/";
			}

			incidenceEdit = incidenceService.getIncidences(id);

			if (incidenceEdit == null) {
				return "/plantilla/404";
			}

			if (!incidenceEdit.getStatus().getStatus().getName().equals("Borrador")) {
				redirectAttributes.addFlashAttribute("data", "Ticket no disponible para editar.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}
			Collection<? extends GrantedAuthority> roles = getUserLogin().getAuthorities();
			Integer countRol = 0;
			for (GrantedAuthority rol : roles) {
				if (rol.getAuthority().equals("ROLE_Admin")) {
					countRol++;
				}
				if (rol.getAuthority().equals("ROLE_Incidencias")) {
					countRol++;
				}

			}

			if (countRol == 0) {
				redirectAttributes.addFlashAttribute("data",
						"No tiene permisos sobre el ticket ya que no formas parte de este equipo.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}
			/*
			 * if (!(rfcEdit.getUser().getUsername().toLowerCase().trim())
			 * .equals((user.getUsername().toLowerCase().trim()))) {
			 * redirectAttributes.addFlashAttribute("data",
			 * "No tiene permisos sobre el rfc."); String referer =
			 * request.getHeader("Referer"); return "redirect:" + referer; }
			 */
			List<System_Priority> priorities = priorityIncidenceService.findBySystem(incidenceEdit.getSystem().getId());
			model.addAttribute("systems", systems);
			model.addAttribute("typeChange", typeChangeService.findAll());
			model.addAttribute("priorities", priorities);
			model.addAttribute("typeincidences", typeIncidenceService.findBySystem(incidenceEdit.getSystem().getId()));
			model.addAttribute("incidence", incidenceEdit);
			model.addAttribute("senders", incidenceEdit.getSenders());
			model.addAttribute("message", incidenceEdit.getMessage());
			if (incidenceEdit.getTypeIncidence() != null) {
				model.addAttribute("ccs", getCC(incidenceEdit.getTypeIncidence().getEmailTemplate().getCc()));
			}
			return "/incidence/editIncidence";

		} catch (Exception e) {
			Sentry.capture(e, "incidence");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/";
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody Incidence addIncidence) {
		JsonResponse res = new JsonResponse();
		try {
			User user = userService.getUserByUsername(getUserLogin().getUsername());
			SystemInfo system = systemService.findById(addIncidence.getSystemId());
			System_StatusIn status = statusService.findByIdByCode(system.getId(), "draft");
			if (status != null) {
				addIncidence.setStatus(status);
				addIncidence.setSlaActive(status.getSlaActive());
				addIncidence.setCreateFor(user.getFullName());
				addIncidence.setSystem(system);
				//addIncidence.setRequestDate(CommonUtils.getSystemTimestamp());
				addIncidence.setUpdateDate(CommonUtils.getSystemTimestamp());
				addIncidence.setMotive("Inicio de ticket");
				addIncidence.setOperator(user.getFullName());
				addIncidence.setTypeIncidence(typeIncidenceService.findById(addIncidence.getTypeIncidenceId()));
				addIncidence.setNumTicket(incidenceService.generatTicketNumber(addIncidence.getSystem().getName(),addIncidence.getTypeIncidence().getTypeIncidence().getCode()));
				addIncidence.setPriority(priorityIncidenceService.findById(addIncidence.getPriorityId()));
				incidenceService.save(addIncidence);
				res.setData(addIncidence.getId().toString());
				res.setStatus("success");
				res.setMessage("Se creo correctamente el ticket!");
			} else {

				res.setStatus("exception");
				res.setMessage("Error al crear ticket comunicarse con los administradores!");
			}

		} catch (Exception e) {
			Sentry.capture(e, "incidence");
			res.setStatus("exception");
			res.setMessage("Error al crear ticket!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@SuppressWarnings("null")
	@RequestMapping(value = "/saveIncidence", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse saveTickets(HttpServletRequest request, @RequestBody Incidence addIncidence) {

		JsonResponse res = new JsonResponse();
		Priority priority = null;
		Impact impact = null;
		TypeChange typeChange = null;
		ArrayList<MyError> errors = new ArrayList<MyError>();
		List<Release_RFC> listRelease = new ArrayList<Release_RFC>();

		try {
			errors = validSections(addIncidence, errors);
			Incidence incidenceMod = incidenceService.getIncidences(addIncidence.getId());
			incidenceMod.setPriority(priorityIncidenceService.findById(addIncidence.getPriorityId()));
			incidenceMod.setTypeIncidence(typeIncidenceService.findById(addIncidence.getTypeIncidenceId()));
			incidenceMod.setNote(addIncidence.getNote());
			incidenceMod.setDetail(addIncidence.getDetail());
			incidenceMod.setTitle(addIncidence.getTitle());
			incidenceMod.setResult(addIncidence.getResult());
			incidenceMod.setSenders(addIncidence.getSenders());
			incidenceMod.setMessage(addIncidence.getMessage());
			/*
			 * if (addRFC.getImpactId() != 0) { impact =
			 * impactService.findById(addRFC.getImpactId()); addRFC.setImpact(impact); }
			 * 
			 * if (addRFC.getPriorityId() != 0) { priority =
			 * priorityService.findById(addRFC.getPriorityId());
			 * addRFC.setPriority(priority); } if (addRFC.getTypeChangeId() != null) {
			 * typeChange = typeChangeService.findById(addRFC.getTypeChangeId());
			 * addRFC.setTypeChange(typeChange);
			 * 
			 * }
			 */
			incidenceService.update(incidenceMod);
			res.setStatus("success");
			if (errors.size() > 0) {
				// Se adjunta lista de errores
				res.setStatus("fail");
				res.setErrors(errors);
			}

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			res.setStatus("exception");
			res.setException("Error al guardar el rfc: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/updateIncidence/{incidenceId}", method = RequestMethod.GET)
	public String updateRFC(@PathVariable String incidenceId, HttpServletRequest request, Locale locale,
			HttpSession session, RedirectAttributes redirectAttributes) {
		try {
			Incidence incidence = null;

			if (CommonUtils.isNumeric(incidenceId)) {
				incidence = incidenceService.getIncidences((long) Integer.parseInt(incidenceId));
			}
			// Si el release no existe se regresa al inicio.
			if (incidence == null) {
				return "redirect:/homeIncidence";
			}
			// Verificar si existe un flujo para el sistema

			System_StatusIn status = statusService.findByIdByName(incidence.getSystem().getId(), "Solicitado");

			if (status == null) {
				redirectAttributes.addFlashAttribute("data",
						"Necesita tener el estado Solicitado asignado al release.");
				return "redirect:/incidence/editIncidence-" + incidence.getId();
			}
			NodeIncidence node = nodeService.existWorkFlowNodeIn(incidence);
//			if (node != null)

//				release.setNode(node);

			incidence.setStatus(status);
			incidence.setSlaActive(status.getSlaActive());
			incidence.setMotive(status.getStatus().getReason());
			incidence.setOperator(getUserLogin().getFullName());

			if(incidence.getRequestDate()==null) {
			incidence.setRequestDate((CommonUtils.getSystemTimestamp()));
			incidence.setUpdateDate((CommonUtils.getSystemTimestamp()));

		
			String[] time = incidence.getPriority().getTime().split(":");
			int hours = Integer.parseInt(time[0]);
			int minutes = Integer.parseInt(time[1]);
			int mili = hours * 3600000 + minutes * 60000;
			incidence.setTimeMili(mili);
			Calendar dayExit = Calendar.getInstance();
			dayExit.setTime(incidence.getRequestDate());
			dayExit.add(Calendar.HOUR, hours);
			dayExit.add(Calendar.MINUTE, minutes);
			Date dateNow = new Date();
			Calendar dayFinish = Calendar.getInstance();
			dayFinish.setTime(dateNow);
			int fiveHour = 17;
			// c.add(Calendar.HOUR, fiveHour);
			dayFinish.set(Calendar.HOUR_OF_DAY, fiveHour);
			dayFinish.set(Calendar.MINUTE, 0);
			dayFinish.set(Calendar.SECOND, 0);
			dayFinish.set(Calendar.MILLISECOND, 0);

			Calendar dayNext = Calendar.getInstance();
			dayNext.setTime(dateNow);
			int eightHour = 8;
			// c.add(Calendar.HOUR, fiveHour);
			dayNext.set(Calendar.HOUR_OF_DAY, eightHour);
			dayNext.set(Calendar.MINUTE, 0);
			dayNext.set(Calendar.SECOND, 0);
			dayNext.set(Calendar.MILLISECOND, 0);
			dayNext.add(Calendar.DAY_OF_YEAR, 1);
			int dayOfWeek = dayNext.get(Calendar.DAY_OF_WEEK);

			// c.add(Calendar.DATE, 1);
			Timestamp timestampDayFive = new Timestamp(dayFinish.getTimeInMillis());

			/*
			 * Date dt = new Date(); Calendar c = Calendar.getInstance(); c.setTime(dt);
			 * c.add(Calendar.DATE, 1); dt = c.getTime();
			 * 
			 * 
			 */
			int res = 0;
			do {
				res = (int) dayExit.getTimeInMillis() - (int) dayFinish.getTimeInMillis();
				if (mili > res) {
					if (res > 0) {
						if (dayNext.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

							dayNext.add(Calendar.DAY_OF_YEAR, 1);

							dayExit.set(Calendar.DATE, dayNext.get(Calendar.DATE));
							dayExit.set(Calendar.HOUR_OF_DAY, eightHour);
							dayExit.set(Calendar.MINUTE, 0);
							dayExit.set(Calendar.SECOND, 0);
							dayExit.set(Calendar.MILLISECOND, 0);
							dayExit.set(Calendar.MILLISECOND, res);
							dayFinish.set(Calendar.DATE, dayNext.get(Calendar.DATE));
							dayFinish.set(Calendar.HOUR_OF_DAY, fiveHour);
							// dayFinish=dayNext;
							Timestamp timestampTest = new Timestamp(dayNext.getTimeInMillis());
							timestampTest = new Timestamp(dayFinish.getTimeInMillis());
							timestampTest = new Timestamp(dayExit.getTimeInMillis());
							java.lang.System.out.println(timestampTest);
							// dayExit=dayNext;
							Timestamp timestampDayNext = new Timestamp(dayNext.getTimeInMillis());
							java.lang.System.out.println("dia siguiente 8 " + timestampDayNext);
							Timestamp timestampOptim = new Timestamp(dayExit.getTimeInMillis());
							java.lang.System.out.println("salida optima" + timestampOptim);
							Timestamp timestampFinishDay = new Timestamp(dayFinish.getTimeInMillis());
							java.lang.System.out
									.println("dia actual 5 debe ser igual al de salida optima" + timestampFinishDay);
							dayOfWeek = dayNext.get(Calendar.DAY_OF_WEEK);
						} else {
							dayExit.set(Calendar.DATE, dayNext.get(Calendar.DATE));
							dayExit.set(Calendar.HOUR_OF_DAY, eightHour);
							dayExit.set(Calendar.MINUTE, 0);
							dayExit.set(Calendar.SECOND, 0);
							dayExit.set(Calendar.MILLISECOND, 0);
							dayExit.set(Calendar.MILLISECOND, res);
							Timestamp timestampTest = new Timestamp(dayExit.getTimeInMillis());
							java.lang.System.out.println(timestampTest);
							java.lang.System.out.println( dayNext.get(Calendar.DATE));

							dayFinish.set(Calendar.DATE, dayNext.get(Calendar.DATE));
							dayFinish.set(Calendar.HOUR_OF_DAY, fiveHour);
							// dayFinish=dayNext;
							timestampTest = new Timestamp(dayExit.getTimeInMillis());
							java.lang.System.out.println(timestampTest);
							java.lang.System.out.println(timestampTest);
							// dayExit=dayNext;
							Timestamp timestampDayNext = new Timestamp(dayNext.getTimeInMillis());
							java.lang.System.out.println("dia siguiente 8 " + timestampDayNext);
							Timestamp timestampOptim = new Timestamp(dayExit.getTimeInMillis());
							java.lang.System.out.println("salida optima" + timestampOptim);
							Timestamp timestampFinishDay = new Timestamp(dayFinish.getTimeInMillis());
							java.lang.System.out
									.println("dia actual 5 debe ser igual al de salida optima" + timestampFinishDay);
							dayNext.add(Calendar.DAY_OF_YEAR, 1);
							dayNext.set(Calendar.HOUR_OF_DAY, eightHour);
							dayNext.set(Calendar.MINUTE, 0);
							dayNext.set(Calendar.SECOND, 0);
							dayNext.set(Calendar.MILLISECOND, 0);
						}
					}
				} else {

					dayExit.setTime(incidence.getRequestDate());
					dayExit.add(Calendar.DAY_OF_YEAR, 1);
					if(dayExit.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY) {
						dayExit.add(Calendar.DAY_OF_YEAR, 2);
						dayExit.set(Calendar.HOUR_OF_DAY, eightHour);
						dayExit.set(Calendar.MINUTE, 0);
						dayExit.set(Calendar.SECOND, 0);
						dayExit.set(Calendar.MILLISECOND, 0);
						
						dayFinish.set(Calendar.DATE, dayExit.get(Calendar.DATE));
						dayFinish.set(Calendar.HOUR_OF_DAY, fiveHour);
						dayFinish.set(Calendar.MINUTE, 0);
						dayFinish.set(Calendar.SECOND, 0);
						dayFinish.set(Calendar.MILLISECOND, 0);
						
						dayNext.set(Calendar.DATE, dayExit.get(Calendar.DATE));
						dayNext.set(Calendar.HOUR_OF_DAY, eightHour);
						dayNext.set(Calendar.MINUTE, 0);
						dayNext.set(Calendar.SECOND, 0);
						dayNext.set(Calendar.MILLISECOND, 0);
						dayNext.add(Calendar.DAY_OF_YEAR, 1);
					}else {
						dayExit.set(Calendar.HOUR_OF_DAY, eightHour);
						dayExit.set(Calendar.MINUTE, 0);
						dayExit.set(Calendar.SECOND, 0);
						dayExit.set(Calendar.MILLISECOND, 0);
						dayFinish.set(Calendar.DATE, dayExit.get(Calendar.DATE));
						dayFinish.set(Calendar.HOUR_OF_DAY, fiveHour);
						dayFinish.set(Calendar.MINUTE, 0);
						dayFinish.set(Calendar.SECOND, 0);
						dayFinish.set(Calendar.MILLISECOND, 0);
						
						dayNext.set(Calendar.DATE, dayExit.get(Calendar.DATE));
						dayNext.set(Calendar.HOUR_OF_DAY, eightHour);
						dayNext.set(Calendar.MINUTE, 0);
						dayNext.set(Calendar.SECOND, 0);
						dayNext.set(Calendar.MILLISECOND, 0);
						dayNext.add(Calendar.DAY_OF_YEAR, 1);
					}
					Timestamp timestampNewRequest = new Timestamp(dayExit.getTimeInMillis());
					incidence.setRequestDate(timestampNewRequest);
					dayExit.add(Calendar.HOUR, hours);
					dayExit.add(Calendar.MINUTE, minutes);
				}
			} while (res > 0);

			Timestamp timestamp = new Timestamp(dayExit.getTimeInMillis());
			incidence.setExitOptimalDate(timestamp);
			}
			if (Boolean.valueOf(parameterService.getParameterByCode(1).getParamValue())) {
				if (incidence.getTypeIncidence().getEmailTemplate() != null) {
					EmailTemplate email = incidence.getTypeIncidence().getEmailTemplate();
					Incidence incidenceEmail = incidence;
					incidenceEmail.setEmail(getUserLogin().getEmail());
					Thread newThread = new Thread(() -> {
						try {
							emailService.sendMailIncidence(incidenceEmail, email);
						} catch (Exception e) {
							Sentry.capture(e, "incidence");
						}

					});
					newThread.start();
				}
			}

			if (node != null) {
				incidence.setNode(node);

				// si tiene un nodo y ademas tiene actor se notifica por correo
				if (node != null && node.getActors().size() > 0) {
					Integer idTemplate = Integer.parseInt(paramService.findByCode(25).getParamValue());
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
					Integer idTemplate = Integer.parseInt(paramService.findByCode(26).getParamValue());

					EmailTemplate emailNotify = emailService.findById(idTemplate);
					WFIncidence incidenceEmail = new WFIncidence();
					incidenceEmail.convertReleaseToWFIncidence(incidence);
					String user = getUserLogin().getFullName();
					Thread newThread = new Thread(() -> {
						try {
							emailService.sendMailNotify(incidenceEmail, emailNotify, user);
						} catch (Exception e) {
							Sentry.capture(e, "release");
						}

					});
					newThread.start();
				}
			} else {
				User userNew = userService.getUserByUsername(getUserLogin().getFullName());
				incidence.setUser(userNew);
				incidence.setAssigned(userNew.getFullName());
			}

			incidenceService.update(incidence);
			return "redirect:/incidence/summaryIncidence-" + incidence.getId();
		} catch (Exception e) {
			Sentry.capture(e, "incidence");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/homeIncidence";
	}

	@RequestMapping(value = "/summaryIncidence-{status}", method = RequestMethod.GET)
	public String summmary(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {
		User user = userService.getUserByUsername(getUserLogin().getUsername());

		try {
			model.addAttribute("parameter", status);
			Incidence incidence = null;
			if (CommonUtils.isNumeric(status)) {
				incidence = incidenceService.getIncidences((Long.parseLong(status)));
			}

			if (incidence == null) {
				return "redirect:/";
			}

			model.addAttribute("incidence", incidence);

		} catch (Exception e) {
			Sentry.capture(e, "incidence");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina resumen incidence." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/homeIncidence";
		}

		return "/incidence/summaryIncidence";
	}

	@SuppressWarnings("null")
	@RequestMapping(value = "/tinySummary-{status}", method = RequestMethod.GET)
	public String tinySummary(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {

		try {
			model.addAttribute("parameter", status);
			Incidence incidence = null;
			if (CommonUtils.isNumeric(status)) {
				incidence = incidenceService.getIncidences((Long.parseLong(status)));
			}

			if (incidence == null) {
				return "redirect:/homeIncidence";
			}

			model.addAttribute("incidence", incidence);

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina resumen ticket." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/";
		}
		return "/incidence/tinySummaryIncidence";
	}

	private ArrayList<MyError> validSections(Incidence addIncidence, ArrayList<MyError> errors) {

		if (addIncidence.getPriorityId() == null) {
			errors.add(new MyError("pId", "Valor requerido."));
		} else {
			if (addIncidence.getPriorityId() == 0)
				errors.add(new MyError("pId", "Valor requerido."));
		}

		if (addIncidence.getTypeIncidenceId() == null) {
			errors.add(new MyError("tId", "Valor requerido."));
		} else {
			if (addIncidence.getTypeIncidenceId() == 0)
				errors.add(new MyError("tId", "Valor requerido."));
		}

		if (addIncidence.getTitle().trim().equals(""))
			errors.add(new MyError("title", "Valor requerido."));

		if (addIncidence.getDetail().trim().equals(""))
			errors.add(new MyError("detail", "Valor requerido."));

		if (addIncidence.getResult().trim().equals(""))
			errors.add(new MyError("result", "Valor requerido."));

		if (addIncidence.getNote().trim().equals(""))
			errors.add(new MyError("note", "Valor requerido."));

		if (addIncidence.getSenders() != null) {
			if (addIncidence.getSenders().length() > 256) {
				errors.add(new MyError("senders", "La cantidad de caracteres no puede ser mayor a 256"));
			} else {
				MyError error = getErrorSenders(addIncidence.getSenders());
				if (error != null) {
					errors.add(error);
				}
			}
		}
		if (addIncidence.getMessage() != null) {
			if (addIncidence.getMessage().length() > 256) {
				errors.add(new MyError("messagePer", "La cantidad de caracteres no puede ser mayor a 256"));
			}
		}

		return errors;
	}

	public MyError getErrorSenders(String senders) {

		String[] listSenders = senders.split(",");
		String to_invalid = "";
		for (int i = 0; i < listSenders.length; i++) {
			if (!CommonUtils.isValidEmailAddress(listSenders[i])) {
				if (to_invalid.equals("")) {
					to_invalid += listSenders[i];
				} else {
					to_invalid += "," + listSenders[i];
				}

			}
		}
		if (!to_invalid.equals("")) {
			return new MyError("senders", "dirección(es) inválida(s) " + to_invalid);
		}
		return null;
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
		// List<PSystem> systems = systemService.listProjects(userLogin.getId());
		Map<String, Integer> userC = new HashMap<String, Integer>();
		userC.put("draft", incidenceService.countByType(id, "Borrador", 1, null, userLogin, email));
		userC.put("requested", incidenceService.countByType(id, "Solicitado", 1, null, userLogin, email));
		userC.put("completed", incidenceService.countByType(id, "Completado", 1, null, userLogin, email));
		userC.put("all", (userC.get("draft") + userC.get("requested") + userC.get("completed")));
		request.setAttribute("userC", userC);

	}

}
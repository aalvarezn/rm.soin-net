package com.soin.sgrm.controller;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Sets;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.Release_RFC;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.Tree;
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.Priority;

import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.ImpactService;
import com.soin.sgrm.service.ParameterService;
import com.soin.sgrm.service.PriorityService;
import com.soin.sgrm.service.RFCService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.SigesService;
import com.soin.sgrm.service.StatusRFCService;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.TypeChange;
import com.soin.sgrm.model.User;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.TreeService;
import com.soin.sgrm.service.TypeChangeService;

import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyError;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping(value = "/rfc")
public class RFCController extends BaseController {
	@Autowired
	PriorityService priorityService;

	@Autowired
	RFCService rfcService;

	@Autowired
	StatusRFCService statusService;
	
	@Autowired
	StatusService statusReleaseService;

	@Autowired
	SystemService systemService;

	@Autowired
	SigesService sigeService;

	@Autowired
	ImpactService impactService;

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
	com.soin.sgrm.service.UserService userService;
	
	public static final Logger logger = Logger.getLogger(RFCController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			Integer userLogin = getUserLogin().getId();
			loadCountsRelease(request, userLogin);
			List<System> systems = systemService.listProjects(getUserLogin().getId());
			List<Priority> priorities = priorityService.list();
			List<StatusRFC> statuses = statusService.findAll();
			List<Impact> impacts = impactService.list();
			model.addAttribute("priorities", priorities);
			model.addAttribute("impacts", impacts);
			model.addAttribute("statuses", statuses);
			model.addAttribute("systems", systems);
		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			e.printStackTrace();
		}
		return "/rfc/rfc";

	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<RFC> rfcs = new JsonSheet<>();
		try {

			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			Integer name = getUserLogin().getId();
			String sSearch = request.getParameter("sSearch");
			 Long statusId;
			 int priorityId;
			 int systemId;
			if (request.getParameter("statusId").equals("")) {
				statusId = null;
			} else {
				statusId = (long) Integer.parseInt(request.getParameter("statusId"));
			}
			if (request.getParameter("priorityId").equals("")) {
				priorityId = 0;
			} else {
				priorityId =  Integer.parseInt(request.getParameter("priorityId"));
			}
			
			if (request.getParameter("systemId").equals("")) {
				systemId = 0;
			} else {
				systemId =  Integer.parseInt(request.getParameter("systemId"));
			}
			String dateRange = request.getParameter("dateRange");

			rfcs = rfcService.findAll2(name,sEcho, iDisplayStart, iDisplayLength, sSearch, statusId, dateRange,priorityId, systemId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rfcs;
	}
	
	@RequestMapping(value = { "/changeProject/{id}" }, method = RequestMethod.GET)
	public @ResponseBody List<Siges> changeProject(@PathVariable Integer id, Locale locale, Model model) {
		List<Siges> codeSiges = null;
		try {
			codeSiges = sigeService.listCodeSiges(id);
		} catch (Exception e) {
			Sentry.capture(e, "siges");

			e.printStackTrace();
		}

		return codeSiges;
	}
	
	@RequestMapping(value = { "/changeRelease" }, method = RequestMethod.GET)
	public @ResponseBody com.soin.sgrm.utils.JsonSheet<?> changeRelease(HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {

		try {
			Integer systemId;
			String sSearch = request.getParameter("sSearch");
			if (request.getParameter("systemId").equals("")) {
				systemId =0;
			} else {
				systemId =  Integer.parseInt(request.getParameter("systemId"));
			}

			int sEcho = Integer.parseInt(request.getParameter("sEcho")),
					iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart")),
					iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			return releaseService.listReleasesBySystem(sEcho, iDisplayStart, iDisplayLength, sSearch, systemId);
		} catch (Exception e) {
			Sentry.capture(e, "siges");

			return null;
		}

	}
	
	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody RFC addRFC) {
		JsonResponse res = new JsonResponse();
		try {
			User user=userService.getUserByUsername(getUserLogin().getUsername());
			StatusRFC status = statusService.findByKey("code", "draft");
			if(status!=null) {
				addRFC.setStatus(status);
				addRFC.setUser(user);
				addRFC.setRequiredBD(false);
				addRFC.setRequestDate(CommonUtils.getSystemTimestamp());
				res.setStatus("success");
				addRFC.setMotive("Inicio de RFC");	
				addRFC.setOperator(user.getFullName());
				Siges codeSiges= sigeService.findByKey("codeSiges", addRFC.getCodeProyect());
				addRFC.setSiges(codeSiges);
				addRFC.setNumRequest(rfcService.generateRFCNumber(addRFC.getCodeProyect()));
				addRFC.setSystemInfo(systemService.findById(addRFC.getSystemId()));
				rfcService.save(addRFC);
				res.setData(addRFC.getId().toString());
				res.setMessage("Se creo correctamente el RFC!");
			}else {
				
				res.setStatus("exception");
				res.setMessage("Error al crear RFC comunicarse con los administradores!");
			}
	
		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			res.setStatus("exception");
			res.setMessage("Error al crear RFC!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	@SuppressWarnings("null")
	@RequestMapping(value = "/saveRFC", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse saveRelease(HttpServletRequest request, @RequestBody RFC addRFC) {

		JsonResponse res = new JsonResponse();
		Priority priority = null;
		Impact impact = null;
		TypeChange typeChange = null;
		ArrayList<MyError> errors = new ArrayList<MyError>();
		List<Release_RFC> listRelease = new ArrayList<Release_RFC>();

		try {
			errors = validSections(addRFC, errors);
			RFC rfcMod = rfcService.findById(addRFC.getId());
			addRFC.setUser(rfcMod.getUser());
			addRFC.setNumRequest(rfcMod.getNumRequest());
			addRFC.setCodeProyect(rfcMod.getCodeProyect());
			addRFC.setStatus(rfcMod.getStatus());
			addRFC.setMotive(rfcMod.getMotive());
			addRFC.setSiges(rfcMod.getSiges());
			addRFC.setSystemInfo(rfcMod.getSystemInfo());
			addRFC.setOperator(rfcMod.getUser().getFullName());
			addRFC.setRequestDate(rfcMod.getRequestDate());
			addRFC.setFiles(rfcMod.getFiles());
			if (addRFC.getImpactId() != 0) {
				impact = impactService.findById(addRFC.getImpactId());
				addRFC.setImpact(impact);
			}

			if (addRFC.getPriorityId() != 0) {
				priority = priorityService.findById(addRFC.getPriorityId());
				addRFC.setPriority(priority);
			}
			if (addRFC.getTypeChangeId() != null) {
				typeChange = typeChangeService.findById(addRFC.getTypeChangeId());
				addRFC.setTypeChange(typeChange);

			}
			if (addRFC.getReleasesList() != null) {
				JSONArray jsonArray = new JSONArray(addRFC.getReleasesList());
				String dbScheme=addRFC.getSchemaDB();
				if (jsonArray.length() != 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object = jsonArray.getJSONObject(i);
						
						Release_RFC release = releaseService.findRelease_RFCById(object.getInt(("id")));
						Set<ReleaseObject> releaseObjects=release.getObjects();
						for(ReleaseObject releaseObject:releaseObjects) {
							
							if (releaseObject.getIsSql() == 1) {
								String scheme=releaseObject.getDbScheme();
								addRFC.setRequiredBD(true);
								if(dbScheme.trim().equals("")) {
									dbScheme=scheme;
								}else {
									String[] split=dbScheme.split(",");
									boolean verify= ArrayUtils.contains(split,scheme);
									if(!verify) {
										dbScheme=dbScheme+","+scheme;
									}
								}
							}
						}
						listRelease.add(release);

					}
					addRFC.setSchemaDB(dbScheme);
					addRFC.setReleases(Sets.newHashSet(listRelease));
				}
			}

			rfcService.update(addRFC);
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

	@RequestMapping(value = "/editRFC-{id}", method = RequestMethod.GET)
	public String editRelease(@PathVariable Long id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		RFC rfcEdit = new RFC();
		User user = userService.getUserByUsername(getUserLogin().getUsername());
		List<System> systems = systemService.listProjects(user.getId());
		try {
			if (id == null) {
				return "redirect:/";
			}

			rfcEdit = rfcService.findById(id);

			if (rfcEdit == null) {
				return "/plantilla/404";
			}

			if (!rfcEdit.getStatus().getName().equals("Borrador")) {
				redirectAttributes.addFlashAttribute("data", "RFC no disponible para editar.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}
			Integer idManager= getUserLogin().getId();
			Integer countByManager= rfcService.countByManager(idManager, rfcEdit.getId());
			if(countByManager==0) {
				redirectAttributes.addFlashAttribute("data", "No tiene permisos sobre el rfc ya que no formas parte de este equipo.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}
/*	
			if (!(rfcEdit.getUser().getUsername().toLowerCase().trim())
					.equals((user.getUsername().toLowerCase().trim()))) {
				redirectAttributes.addFlashAttribute("data", "No tiene permisos sobre el rfc.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}
			*/
			Set<Release_RFC> releases = rfcEdit.getReleases();
			for(Release_RFC release:releases) {
				release.setHaveDependecy(releaseService.getDependency(release.getId()));
			}
			
			model.addAttribute("systems", systems);
			model.addAttribute("impacts", impactService.list());
			model.addAttribute("typeChange", typeChangeService.findAll());
			model.addAttribute("priorities", priorityService.list());
			model.addAttribute("rfc", rfcEdit);
			model.addAttribute("senders", rfcEdit.getSenders());
			model.addAttribute("message", rfcEdit.getMessage());
			model.addAttribute("ccs", getCC(rfcEdit.getSiges().getEmailTemplate().getCc()));

			return "/rfc/editRFC";

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/";
	}
	
	@RequestMapping(value = "/getRFC-{id}", method = RequestMethod.GET)
	public @ResponseBody RFC getRFC(@PathVariable Long id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		RFC rfcEdit = new RFC();

		try {

			rfcEdit = rfcService.findById(id);
			Set<Release_RFC> releases = rfcEdit.getReleases();
			for(Release_RFC release:releases) {
				release.setHaveDependecy(releaseService.getDependency(release.getId()));
			}
			return rfcEdit;

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return rfcEdit;
	}

	@RequestMapping(value = "/summaryRFC-{status}", method = RequestMethod.GET)
	public String summmary(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {
		User user =  userService.getUserByUsername(getUserLogin().getUsername());
		List<System> systems = systemService.listProjects(user.getId());
		try {
			model.addAttribute("parameter", status);
			RFC rfc = null;
			if (CommonUtils.isNumeric(status)) {
				rfc = rfcService.findById(Long.parseLong(status));
			}

			if (rfc == null) {
				return "redirect:/";
			}
			Siges codeSiges = sigeService.findByKey("codeSiges", rfc.getCodeProyect());

			List<String> systemsImplicated = new ArrayList<String>();

			systemsImplicated.add(codeSiges.getSystem().getName());
			String nameSystem = "";
			boolean validate = true;
			Set<Release_RFC> releases = rfc.getReleases();
			if (releases != null) {
				if (releases.size() != 0) {
					for (Release_RFC release : releases) {
						nameSystem = release.getSystem().getName();
						for (String system : systemsImplicated) {
							if (system.equals(nameSystem)) {
								validate = false;
							}
						}
						if (validate) {
							systemsImplicated.add(nameSystem);
						}
						validate = true;
					}
				}

			}
			model.addAttribute("statuses", statusService.findAll());
			model.addAttribute("systems", systems);
			model.addAttribute("impacts", impactService.list());
			model.addAttribute("typeChange", typeChangeService.findAll());
			model.addAttribute("priorities", priorityService.list());
			model.addAttribute("codeSiges", codeSiges);
			model.addAttribute("systemsImplicated", systemsImplicated);
			model.addAttribute("rfc", rfc);

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina resumen rfc." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/";
		}

		return "/rfc/summaryRFC";
	}
	@SuppressWarnings("null")
	@RequestMapping(value = "/tinySummary-{status}", method = RequestMethod.GET)
	public String tinySummary(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {
		User user =userService.getUserByUsername(getUserLogin().getUsername());
		List<System> systems = systemService.listProjects(user.getId());
		try {
			model.addAttribute("parameter", status);
			RFC rfc = null;
			if (CommonUtils.isNumeric(status)) {
				rfc = rfcService.findById(Long.parseLong(status));
			}

			if (rfc == null) {
				return "redirect:/";
			}
			Siges codeSiges = sigeService.findByKey("codeSiges", rfc.getCodeProyect());

			List<String> systemsImplicated = new ArrayList<String>();

			systemsImplicated.add(codeSiges.getSystem().getName());
			String nameSystem = "";
			boolean validate = true;
			Set<Release_RFC> releases = rfc.getReleases();
			if (releases != null) {
				if (releases.size() != 0) {
					for (Release_RFC release : releases) {
						nameSystem = release.getSystem().getName();
						for (String system : systemsImplicated) {
							if (system.equals(nameSystem)) {
								validate = false;
							}
						}
						if (validate) {
							systemsImplicated.add(nameSystem);
						}
						validate = true;
					}
				}

			}
			model.addAttribute("systems", systems);
			model.addAttribute("impacts", impactService.list());
			model.addAttribute("typeChange", typeChangeService.findAll());
			model.addAttribute("priorities", priorityService.list());
			model.addAttribute("codeSiges", codeSiges);
			model.addAttribute("systemsImplicated", systemsImplicated);
			model.addAttribute("rfc", rfc);

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina resumen rfc." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/";
		}
		return "/rfc/tinySummaryRFC";
	}
	
	@RequestMapping(value = "/updateRFC/{rfcId}", method = RequestMethod.GET)
	public String updateRFC(@PathVariable String rfcId, HttpServletRequest request, Locale locale, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			RFC rfc = null;

			if (CommonUtils.isNumeric(rfcId)) {
				rfc = rfcService.findById((long) Integer.parseInt(rfcId));
			}
			// Si el release no existe se regresa al inicio.
			if (rfc == null) {
				return "redirect:/homeRFC";
			}
			// Verificar si existe un flujo para el sistema

			StatusRFC status = statusService.findByKey("name", "Solicitado");

//			if (node != null)
			
//				release.setNode(node);

			rfc.setStatus(status);
			rfc.setMotive(status.getReason());
			rfc.setRequestDate((CommonUtils.getSystemTimestamp()));
			
			 rfc.setOperator(getUserLogin().getFullName());
			 Siges siges=sigeService.findById(rfc.getSiges().getId());
			if (Boolean.valueOf(parameterService.getParameterByCode(1).getParamValue())) {
				if (siges.getEmailTemplate() != null) {
					EmailTemplate email = siges.getEmailTemplate();
					RFC rfcEmail = rfc;
					Thread newThread = new Thread(() -> {
						try {
							emailService.sendMailRFC(rfcEmail, email);
						} catch (Exception e) {
							Sentry.capture(e, "rfc");
						}

					});
					newThread.start();
				}
			}
			/*
			 * // si tiene un nodo y ademas tiene actor se notifica por correo if (node !=
			 * null && node.getActors().size() > 0) { Integer idTemplate =
			 * Integer.parseInt(paramService.findByCode(22).getParamValue()); EmailTemplate
			 * emailActor = emailService.findById(idTemplate); WFRelease releaseEmail = new
			 * WFRelease(); releaseEmail.convertReleaseToWFRelease(release); Thread
			 * newThread = new Thread(() -> { try { emailService.sendMailActor(releaseEmail,
			 * emailActor); } catch (Exception e) { Sentry.capture(e, "release"); }
			 * 
			 * });
			 * 
			 * newThread.start(); }
			 */

			rfcService.update(rfc);
			Set<Release_RFC> releases=  rfc.getReleases();
			String user=getUserLogin().getFullName();
			for(Release_RFC release: releases) {
				release.setStatusBefore(release.getStatus());
				Status statusRelease= statusReleaseService.findByName("RFC");
				release.setStatus(statusRelease);
				release.setMotive(statusRelease.getMotive());
				releaseService.updateStatusReleaseRFC(release,user);
			}
			
			return "redirect:/rfc/summaryRFC-" + rfc.getId();
		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/homeRFC";
	}
	
	public ArrayList<MyError> validSections(RFC rfc, ArrayList<MyError> errors) {

		if (rfc.getImpactId()==0)
			errors.add(new MyError("impactId", "Valor requerido."));
		if (rfc.getTypeChangeId()==null ) {
			errors.add(new MyError("typeChangeId", "Valor requerido."));
		}else {
			if (rfc.getTypeChangeId() ==0 )
				errors.add(new MyError("typeChangeId", "Valor requerido."));
		}
			
		if (rfc.getPriorityId() ==0)
			errors.add(new MyError("priorityId", "Valor requerido."));

		if (rfc.getRequestDateBegin().trim().equals(""))
			errors.add(new MyError("dateBegin", "Valor requerido."));

		if (rfc.getRequestDateBegin().trim().equals(""))
			errors.add(new MyError("dateFinish", "Valor requerido."));

		if (rfc.getReasonChange().trim().equals(""))
			errors.add(new MyError("rfcReason", "Valor requerido."));

		if (rfc.getEffect().trim().equals(""))
			errors.add(new MyError("rfcEffect", "Valor requerido."));

		if (rfc.getDetail().trim().equals(""))
			errors.add(new MyError("detailRFC", "Valor requerido."));

		if (rfc.getReturnPlan().trim().equals(""))
			errors.add(new MyError("returnPlanRFC", "Valor requerido."));

		if (rfc.getEvidence().trim().equals(""))
			errors.add(new MyError("evidenceRFC", "Valor requerido."));

		if (rfc.getRequestEsp().equals(""))
			errors.add(new MyError("requestEspRFC", "Valor requerido."));
		
		if (rfc.getSenders() != null) {
			if (rfc.getSenders().length() > 256) {
				errors.add(new MyError("senders", "La cantidad de caracteres no puede ser mayor a 256"));
			}else {
				MyError error=getErrorSenders(rfc.getSenders());
				if(error!=null) {
					errors.add(error);
				}
			}
		}
		if (rfc.getMessage() != null) {
			if (rfc.getMessage().length() > 256) {
				errors.add(new MyError("messagePer", "La cantidad de caracteres no puede ser mayor a 256"));
			}
		}
		
		return errors;
	}
	public MyError getErrorSenders(String senders) {
		
		String[] listSenders = senders.split(",");
		String to_invalid="";
		for (int i = 0; i < listSenders.length; i++) {
			if (!CommonUtils.isValidEmailAddress(listSenders[i])) {
				if(to_invalid.equals("")) {
					to_invalid +=listSenders[i];
				}else {
					to_invalid +=","+listSenders[i];
				}
				
			}
		}
		if (!to_invalid.equals("")) {
			return new MyError("senders", "dirección(es) inválida(s) " + to_invalid);	
		}
		return null;
	}
	
	@RequestMapping(value = "/deleteRFC/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteRFC(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			RFC rfc = rfcService.findById(id);
			if (rfc.getStatus().getName().equals("Borrador")) {
				if (rfc.getUser().getUsername().equals(getUserLogin().getUsername())) {
					StatusRFC status = statusService.findByKey("name", "Anulado");
					rfc.setStatus(status);
					rfc.setMotive(status.getReason());
					rfc.setOperator(getUserLogin().getFullName());
					rfcService.update(rfc);
				} else {
					res.setStatus("fail");
					res.setException("No tiene permisos sobre el rfc.");
				}
			} else {
				res.setStatus("fail");
				res.setException("La acción no se pudo completar, el release no esta en estado de Borrador.");
			}
		} catch (Exception e) {
			Sentry.capture(e, "release");
			res.setStatus("exception");
			res.setException("La acción no se pudo completar correctamente.");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	public void loadCountsRelease(HttpServletRequest request, Integer id) {
		//PUser userLogin = getUserLogin();
		//List<PSystem> systems = systemService.listProjects(userLogin.getId());
		Map<String, Integer> userC = new HashMap<String, Integer>();
		userC.put("draft", rfcService.countByType(id, "Borrador", 1, null));
		userC.put("requested", rfcService.countByType(id, "Solicitado", 1, null));
		userC.put("completed", rfcService.countByType(id, "Completado", 1, null));
		userC.put("all", (userC.get("draft") + userC.get("requested") + userC.get("completed")));
		request.setAttribute("userC", userC);
		
	}
	

	@RequestMapping(value = "/tree/{releaseNumber}/{depth}", method = RequestMethod.GET)
	public @ResponseBody JsonResponse tree(@PathVariable String releaseNumber, @PathVariable Integer depth,
			HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			List<Tree> treeList = treeService.findTree(releaseNumber, depth);
			res.setStatus("success");
			res.setObj(treeList);
		} catch (Exception e) {
			Sentry.capture(e, "admin");
			res.setStatus("exception");
			res.setException("Error al procesar consulta: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	

	public List<String> getCC(String ccs) {
		
		List<String> getCC = new ArrayList<String>();
		if(ccs!=null) {
			ccs.split(",");
			for (String cc : ccs.split(",")) {
				getCC.add(cc);
				}
		}
		return getCC;
	}
	
}

package com.soin.sgrm.controller;


import java.sql.SQLException;
import java.sql.Timestamp;
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
import com.soin.sgrm.model.BaseKnowledge;
import com.soin.sgrm.model.Component;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.Release_RFC;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.EmailReadService;
import com.soin.sgrm.service.AttentionGroupService;
import com.soin.sgrm.service.BaseKnowledgeService;
import com.soin.sgrm.service.ComponentService;
import com.soin.sgrm.service.StatusKnowlegeService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.model.StatusKnowlege;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.System_StatusIn;
import com.soin.sgrm.model.User;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyError;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping(value = "/baseKnowledge")
public class BaseKnowledgeController extends BaseController {
	

	@Autowired
	BaseKnowledgeService baseKnowledgeService;
	
	@Autowired
	StatusKnowlegeService statusService;
	

	@Autowired
	ComponentService componentService;

	
	@Autowired
	com.soin.sgrm.service.UserService userService;
	
	@Autowired
	EmailReadService emailReadService;
	
	@Autowired
	AttentionGroupService attentionGroupService;
	
	@Autowired
	SystemService systemService;
	

	
	public static final Logger logger = Logger.getLogger(BaseKnowledgeController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			Integer userLogin = getUserLogin().getId();
			loadCountsRelease(request, userLogin);
			List<Component> components = componentService.findAll();
			List<StatusKnowlege> statuses = statusService.findAll();
			List<AttentionGroup> attentionGroups= attentionGroupService.findGroupByUserId(userLogin);
			List<Long> listAttentionGroupId=new ArrayList<Long>();
			for(AttentionGroup attentionGroup: attentionGroups) {
				listAttentionGroupId.add(attentionGroup.getId());
			}
			List<System> systemList=systemService.findByGroupIncidence(listAttentionGroupId);
			Set<System> systemWithRepeat = new LinkedHashSet<>(systemList);
			systemList.clear();
			systemList.addAll(systemWithRepeat);
			
			model.addAttribute("statuses", statuses);
			model.addAttribute("system", systemList);
			model.addAttribute("components", components);
		} catch (Exception e) {
			Sentry.capture(e, "baseKnowledge");
			e.printStackTrace();
		}
		return "/incidence/baseKnowledge/baseKnowledge";

	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<BaseKnowledge> baseKnowledges = new JsonSheet<>();
		try {
			Integer userLogin = getUserLogin().getId();
			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			Integer name = getUserLogin().getId();
			String sSearch = request.getParameter("sSearch");
			 Long statusId;
			 Long componentId;
			 Integer systemId;
			if (request.getParameter("statusId").equals("")) {
				statusId = null;
			} else {
				statusId = (long) Integer.parseInt(request.getParameter("statusId"));
			}
			if (request.getParameter("componentId").equals("")) {
				componentId = null;
			} else {
				componentId = (long) Integer.parseInt(request.getParameter("componentId"));
			}
			if (request.getParameter("systemId").equals("")) {
				systemId = null;
			} else {
				systemId =  Integer.parseInt(request.getParameter("systemId"));
			}
			if(systemId==0) {
				componentId=(long)0;
			}
			String dateRange = request.getParameter("dateRange");

			baseKnowledges = baseKnowledgeService.findAll2(name,sEcho, iDisplayStart, iDisplayLength, sSearch, statusId, dateRange,componentId,systemId,userLogin);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return baseKnowledges;
	}

	
	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody BaseKnowledge addBaseKnowledge) {
		JsonResponse res = new JsonResponse();
		try {
			User user=userService.getUserByUsername(getUserLogin().getUsername());
			StatusKnowlege status = statusService.findByKey("code", "draft");
			if(status!=null) {
				addBaseKnowledge.setStatus(status);
				addBaseKnowledge.setUser(user);
				addBaseKnowledge.setPublicate(false);
				SystemInfo system=new SystemInfo();
				system.setId(addBaseKnowledge.getSystemId());
				addBaseKnowledge.setSystem(system);
				addBaseKnowledge.setUrl("/wrk2/app_docs_gestInc");
				addBaseKnowledge.setRequestDate(CommonUtils.getSystemTimestamp());
				res.setStatus("success");
				addBaseKnowledge.setMotive("Inicio ");	
				addBaseKnowledge.setOperator(user.getFullName());
				Component component=componentService.findById(addBaseKnowledge.getComponentId());
				addBaseKnowledge.setComponent(component);
				addBaseKnowledge.setNumError((baseKnowledgeService.generateErrorNumber(component.getName())));
				baseKnowledgeService.save(addBaseKnowledge);
				res.setData(addBaseKnowledge.getId().toString());
				res.setMessage("Se creo correctamente el error!");
			}else {
				
				res.setStatus("exception");
				res.setMessage("Error al crear error comunicarse con los administradores!");
			}
	
		} catch (Exception e) {
			Sentry.capture(e, "baseKnowledge");
			res.setStatus("exception");
			res.setMessage("Error al crear baseKnowledge!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	@SuppressWarnings("null")
	@RequestMapping(value = "/saveBaseKnowledge", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse saveRelease(HttpServletRequest request, @RequestBody BaseKnowledge addBaseKnowledge) {

		JsonResponse res = new JsonResponse();
		ArrayList<MyError> errors = new ArrayList<MyError>();
	

		try {
			errors = validSections(addBaseKnowledge, errors);
			BaseKnowledge baseKnowledgeMod = baseKnowledgeService.findById(addBaseKnowledge.getId());

			addBaseKnowledge.setUser(baseKnowledgeMod.getUser());
			addBaseKnowledge.setNumError(baseKnowledgeMod.getNumError());
			addBaseKnowledge.setComponent(baseKnowledgeMod.getComponent());
			addBaseKnowledge.setStatus(baseKnowledgeMod.getStatus());
			addBaseKnowledge.setMotive(baseKnowledgeMod.getMotive());
			addBaseKnowledge.setUrl(baseKnowledgeMod.getUrl());
			addBaseKnowledge.setOperator(baseKnowledgeMod.getUser().getFullName());
			addBaseKnowledge.setRequestDate(baseKnowledgeMod.getRequestDate());
			addBaseKnowledge.setFiles(baseKnowledgeMod.getFiles());
			if(addBaseKnowledge.getPublicateValidate()==1) {
				addBaseKnowledge.setPublicate(true);
			}else {
				addBaseKnowledge.setPublicate(false);
			}
			
			if (addBaseKnowledge.getDescription().length() < 256) {
				addBaseKnowledge.setDescription(addBaseKnowledge.getDescription());
			} else {
				addBaseKnowledge.setDescription(baseKnowledgeMod.getDescription());
			}
	
			baseKnowledgeService.update(addBaseKnowledge);
			res.setStatus("success");
			if (errors.size() > 0) {
				// Se adjunta lista de errores
				res.setStatus("fail");
				res.setErrors(errors);
			}

		} catch (Exception e) {
			Sentry.capture(e, "baseKnowledge");
			res.setStatus("exception");
			res.setException("Error al guardar el error: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/editBaseKnowledge-{id}", method = RequestMethod.GET)
	public String editRelease(@PathVariable Long id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		BaseKnowledge baseKnowledgeEdit = new BaseKnowledge();
		User user = userService.getUserByUsername(getUserLogin().getUsername());

		try {
			if (id == null) {
				return "redirect:/";
			}

			baseKnowledgeEdit = baseKnowledgeService.findById(id);

			if (baseKnowledgeEdit == null) {
				return "/plantilla/404";
			}

			if (!baseKnowledgeEdit.getStatus().getName().equals("Borrador")) {
				redirectAttributes.addFlashAttribute("data", "error no disponible para editar.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}
			/*
			Integer idManager= getUserLogin().getId();
			Integer countByManager= BaseKnowledgeService.countByManager(idManager, BaseKnowledgeEdit.getId());
			if(countByManager==0) {
				redirectAttributes.addFlashAttribute("data", "No tiene permisos sobre el BaseKnowledge ya que no formas parte de este equipo.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}
			if (!(BaseKnowledgeEdit.getUser().getUsername().toLowerCase().trim())
					.equals((user.getUsername().toLowerCase().trim()))) {
				redirectAttributes.addFlashAttribute("data", "No tiene permisos sobre el BaseKnowledge.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}
			*/
			
			model.addAttribute("baseKnowledge", baseKnowledgeEdit);
	
			return "/incidence/baseKnowledge/editBaseKnowledge";

		} catch (Exception e) {
			Sentry.capture(e, "baseKnowledge");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/";
	}
	
	@RequestMapping(value = "/getBaseKnowledge-{id}", method = RequestMethod.GET)
	public @ResponseBody BaseKnowledge getBaseKnowledge(@PathVariable Long id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		BaseKnowledge baseKnowledgeEdit = new BaseKnowledge();

		try {

			baseKnowledgeEdit = baseKnowledgeService.findById(id);
			
			return baseKnowledgeEdit;

		} catch (Exception e) {
			Sentry.capture(e, "baseKnowledge");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return baseKnowledgeEdit;
	}

	@RequestMapping(value = "/summaryBaseKnowledge-{status}", method = RequestMethod.GET)
	public String summmary(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {
		User user =  userService.getUserByUsername(getUserLogin().getUsername());

		try {
			model.addAttribute("parameter", status);
			BaseKnowledge baseKnowledge = null;
			if (CommonUtils.isNumeric(status)) {
				baseKnowledge = baseKnowledgeService.findById(Long.parseLong(status));
			}

			if (baseKnowledge == null) {
				return "redirect:/";
			}
			model.addAttribute("statuses", statusService.findAll());
			model.addAttribute("baseKnowledge",baseKnowledge);
	
		} catch (Exception e) {
			Sentry.capture(e, "baseKnowledge");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina resumen baseKnowledge." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/";
		}

		return "/incidence/baseKnowledge/summaryBaseKnowledge";
	}
	@SuppressWarnings("null")
	@RequestMapping(value = "/tinySummary-{status}", method = RequestMethod.GET)
	public String tinySummary(@PathVariable String status, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {
		User user =userService.getUserByUsername(getUserLogin().getUsername());
		try {
			model.addAttribute("parameter", status);
			BaseKnowledge baseKnowledge = null;
			if (CommonUtils.isNumeric(status)) {
				baseKnowledge = baseKnowledgeService.findById(Long.parseLong(status));
			}

			if (baseKnowledge == null) {
				return "redirect:/";
			}
			model.addAttribute("baseKnowledge", baseKnowledge);

		} catch (Exception e) {
			Sentry.capture(e, "baseKnowledge");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina resumen error." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/";
		}
		return "/incidence/baseKnowledge/tinySummaryBaseKnowledge";
	}
	
	@RequestMapping(value = "/updateBaseKnowledge/{baseKnowledgeId}", method = RequestMethod.GET)
	public String updateBaseKnowledge(@PathVariable String baseKnowledgeId, HttpServletRequest request, Locale locale, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			BaseKnowledge baseKnowledge = null;

			if (CommonUtils.isNumeric(baseKnowledgeId)) {
				baseKnowledge = baseKnowledgeService.findById((long) Integer.parseInt(baseKnowledgeId));
			}
			// Si el release no existe se regresa al inicio.
			if (baseKnowledge == null) {
				return "redirect:/homeBaseKnowledge";
			}
			// Verificar si existe un flujo para el sistema

			StatusKnowlege status = statusService.findByKey("code", "valid");

//			if (node != null)
			
//				release.setNode(node);

			baseKnowledge.setStatus(status);
			baseKnowledge.setMotive(status.getReason());
			baseKnowledge.setRequestDate((CommonUtils.getSystemTimestamp()));
			
			 baseKnowledge.setOperator(getUserLogin().getFullName());
			/*
			if (Boolean.valueOf(parameterService.getParameterByCode(1).getParamValue())) {
				if (siges.getEmailTemplate() != null) {
					EmailTemplate email = siges.getEmailTemplate();
					BaseKnowledge BaseKnowledgeEmail = BaseKnowledge;
					Thread newThread = new Thread(() -> {
						try {
							emailService.sendMailBaseKnowledge(BaseKnowledgeEmail, email);
						} catch (Exception e) {
							Sentry.capture(e, "BaseKnowledge");
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

			baseKnowledgeService.update(baseKnowledge);
			
	
			return "redirect:/baseKnowledge/summaryBaseKnowledge-" + baseKnowledge.getId();
		} catch (Exception e) {
			Sentry.capture(e, "BaseKnowledge");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/homeBaseKnowledge";
	}
	
	public ArrayList<MyError> validSections(BaseKnowledge baseKnowledge, ArrayList<MyError> errors) {
			
		
		if (baseKnowledge.getDescription() != null) {
			if(baseKnowledge.getDescription().trim().equals("")) {
	
					errors.add(new MyError("description", "Debe ingresar algun dato"));
				
			}else {
			if (baseKnowledge.getDescription().length() > 256) {
				errors.add(new MyError("description", "La cantidad de caracteres no puede ser mayor a 256"));
			}
			}
		}else {
			errors.add(new MyError("description", "Debe ingresar algun dato"));
		}
		
		if (baseKnowledge.getDataRequired()!= null) {
			if(baseKnowledge.getDataRequired().trim().equals("")) {
	
					errors.add(new MyError("data", "Debe ingresar algun dato"));
				
			}
		}else {
			errors.add(new MyError("data", "Debe ingresar algun dato"));
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
	@RequestMapping(value = "/cancelError", method = RequestMethod.GET)
	public @ResponseBody JsonResponse cancelRelease(HttpServletRequest request, Model model,
			@RequestParam(value = "idError", required = true) Long idError) {
		JsonResponse res = new JsonResponse();
		try {
			BaseKnowledge baseKnowledge = baseKnowledgeService.findById(idError);
			StatusKnowlege status = statusService.findByKey("name", "Obsoleto");
			baseKnowledge.setStatus(status);
			baseKnowledge.setOperator(getUserLogin().getFullName());
			baseKnowledge.setMotive(status.getReason());
			baseKnowledgeService.update(baseKnowledge);
			res.setStatus("success");

		} catch (Exception e) {
			Sentry.capture(e, "base");
			res.setStatus("exception");
			res.setException("Error al cancelar el error: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	@RequestMapping(value = "/statusError", method = RequestMethod.GET)
	public @ResponseBody JsonResponse changeStatusError(HttpServletRequest request, Model model,
			@RequestParam(value = "idError", required = true) Long idError,
			@RequestParam(value = "idStatus", required = true) Long idStatus,
			@RequestParam(value = "dateChange", required = true) String dateChange,
			@RequestParam(value = "motive", required = true) String motive) {
		JsonResponse res = new JsonResponse();
		try {
			BaseKnowledge baseKnowledge = baseKnowledgeService.findById(idError);
			StatusKnowlege status = statusService.findById(idStatus);
			String user = getUserLogin().getFullName();

			baseKnowledge.setStatus(status);
			baseKnowledge.setOperator(getUserLogin().getFullName());
			Timestamp dateFormat = CommonUtils.convertStringToTimestamp(dateChange, "dd/MM/yyyy hh:mm a");
			baseKnowledge.setRequestDate(dateFormat);
			baseKnowledge.setMotive(motive);
			baseKnowledgeService.update(baseKnowledge);
			res.setStatus("success");

		} catch (Exception e) {
			Sentry.capture(e, "rfcManagement");
			res.setStatus("exception");
			res.setException("Error al cambiar estado del error: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	@RequestMapping(value = "/deleteBaseKnowledge/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteBaseKnowledge(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			BaseKnowledge baseKnowledge = baseKnowledgeService.findById(id);
			if (baseKnowledge.getStatus().getName().equals("Borrador")) {
				if (baseKnowledge.getUser().getUsername().equals(getUserLogin().getUsername())) {
					StatusKnowlege status = statusService.findByKey("name", "Obsoleto");
					baseKnowledge.setStatus(status);
					baseKnowledge.setMotive(status.getReason());
					baseKnowledge.setOperator(getUserLogin().getFullName());
					baseKnowledgeService.update(baseKnowledge);
				} else {
					res.setStatus("fail");
					res.setException("No tiene permisos sobre el error.");
				}
			} else {
				res.setStatus("fail");
				res.setException("La acción no se pudo completar, el error no esta en estado de Borrador.");
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
		
		userC.put("draft", baseKnowledgeService.countByType(id, "Borrador", 1, null));
		userC.put("requested", baseKnowledgeService.countByType(id, "Obsoleto", 1, null));
		userC.put("completed", baseKnowledgeService.countByType(id, "Vigente", 1, null));
		userC.put("all", (userC.get("draft") + userC.get("requested") + userC.get("completed")));
		request.setAttribute("userC", userC);
		
	}
	
	@RequestMapping(value = { "/getComponent/{id}" }, method = RequestMethod.GET)
	public @ResponseBody List<Component> getPriority(@PathVariable Integer id, Locale locale, Model model) {
		List<Component> listSystemStatus=null;
		try {
			listSystemStatus = componentService.findBySystem(id);
			
		} catch (Exception e) {
			Sentry.capture(e, "systemStatus");

			e.printStackTrace();
		}

		return listSystemStatus;
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

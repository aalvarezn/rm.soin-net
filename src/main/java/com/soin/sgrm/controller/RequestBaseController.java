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
import com.google.gdata.client.appsforyourdomain.UserService;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.Release_RFC;
import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.RequestRM_P1_R4;
import com.soin.sgrm.model.RequestRM_P1_R5;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.StatusRequest;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.TypeChange;
import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.model.TypeRequest;
import com.soin.sgrm.model.User;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.AmbientService;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.ParameterService;
import com.soin.sgrm.service.RequestBaseService;
import com.soin.sgrm.service.RequestRM_P1_R4Service;
import com.soin.sgrm.service.RequestRM_P1_R5Service;
import com.soin.sgrm.service.SigesService;
import com.soin.sgrm.service.StatusRequestService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.TypePetitionService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyError;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping(value = "/request")
public class RequestBaseController extends BaseController{
	
	@Autowired
	SystemService systemService;

	@Autowired
	SigesService sigeService;
	
	@Autowired
	StatusRequestService statusService;
	
	@Autowired
	RequestBaseService requestBaseService;
	
	@Autowired
	TypePetitionService typePetitionService;
	
	@Autowired
	com.soin.sgrm.service.UserService userService;
	
	@Autowired
	RequestRM_P1_R4Service requestServiceRm4;
	@Autowired
	RequestRM_P1_R5Service requestServiceRm5;
	
	@Autowired
	AmbientService ambientService;
	
	@Autowired
	EmailTemplateService emailService;
	
	@Autowired
	ParameterService parameterService;
	
	
	public static final Logger logger = Logger.getLogger(RFCController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			Integer userLogin = getUserLogin().getId();
			loadCountsRelease(request, userLogin);
			List<System> systems = systemService.listProjects(getUserLogin().getId());
			List<StatusRequest> statuses = statusService.findAll();
			List<TypePetition> typePetitions=typePetitionService.findAll();
			model.addAttribute("statuses", statuses);
			model.addAttribute("typePetitions",typePetitions);
			model.addAttribute("systems", systems);
		} catch (Exception e) {
			Sentry.capture(e, "request");
			e.printStackTrace();
		}
		return "/request/request";
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<RequestBase> requests = new JsonSheet<>();
		try {

			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			Integer name = getUserLogin().getId();
			String sSearch = request.getParameter("sSearch");
			Long statusId;
			Integer systemId;
			Long typePetitionId;
			//int priorityId;
			//int systemId;
			if (request.getParameter("statusId").equals("")) {
				statusId = null;
			} else {
				statusId = (long) Integer.parseInt(request.getParameter("statusId"));
			}
			
			if (request.getParameter("typePetitionId").equals("")) {
				typePetitionId = null;
			} else {
				typePetitionId = (long) Integer.parseInt(request.getParameter("typePetitionId"));
			}
			
			if (request.getParameter("systemId").equals("")) {
				systemId = 0;
			} else {
				systemId =  Integer.parseInt(request.getParameter("systemId"));
			}
			
	
			String dateRange = request.getParameter("dateRange");

			requests = requestBaseService.findAllRequest(name,sEcho, iDisplayStart, iDisplayLength, sSearch, statusId, dateRange,systemId,typePetitionId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return requests;
	}
	
	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody RequestBase addRequest) {
		JsonResponse res = new JsonResponse();
		try {
			User user=userService.getUserByUsername(getUserLogin().getUsername());
			StatusRequest status = statusService.findByKey("code", "draft");
			if(status!=null) {
				addRequest.setStatus(status);
				addRequest.setUser(user);
				addRequest.setRequestDate(CommonUtils.getSystemTimestamp());
				res.setStatus("success");
				addRequest.setMotive("Inicio de Solicitud");	
				addRequest.setOperator(user.getFullName());
				Siges codeSiges= sigeService.findByKey("codeSiges", addRequest.getCodeProyect());
				addRequest.setSiges(codeSiges);
				addRequest.setTypePetition(typePetitionService.findById(addRequest.getTypePetitionId()));
				addRequest.setNumRequest(requestBaseService.generateRequestNumber(addRequest.getCodeProyect(),addRequest.getDescription()));
				addRequest.setSystemInfo(systemService.findById(addRequest.getSystemId()));
				requestBaseService.save(addRequest);
				if(addRequest.getTypePetition().getCode().equals("RM-P1-R5")) {
					RequestRM_P1_R5 requestR5=new RequestRM_P1_R5();
					requestR5.setRequestBase(addRequest);
					requestServiceRm5.save(requestR5);
				}
				res.setData(addRequest.getId().toString());
				res.setMessage("Se creo correctamente la solicitud!");
			}else {
				
				res.setStatus("exception");
				res.setMessage("Error al crear la solicitud comunicarse con los administradores!");
			}
	
		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			res.setStatus("exception");
			res.setMessage("Error al crear RFC!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	@RequestMapping(value = "/editRequest-{id}", method = RequestMethod.GET)
	public String editRelease(@PathVariable Long id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		RequestBase requestEdit = new RequestBase();
		User user = userService.getUserByUsername(getUserLogin().getUsername());
		List<System> systems = systemService.listProjects(user.getId());
		try {
			if (id == null) {
				return "redirect:/";
			}

			requestEdit = requestBaseService.findById(id);

			if (requestEdit == null) {
				return "/plantilla/404";
			}

			if (!requestEdit.getStatus().getName().equals("Borrador")) {
				redirectAttributes.addFlashAttribute("data", "Solicitud no disponible para editar.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}
			//Integer idManager= getUserLogin().getId();
			/*
			Integer countByManager= requestBaseService.countByManager(idManager, requestEdit.getId());
			if(countByManager==0) {
				redirectAttributes.addFlashAttribute("data", "No tiene permisos sobre la  ya que no formas parte de este equipo.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}*/
			
			
/*	
			if (!(rfcEdit.getUser().getUsername().toLowerCase().trim())
					.equals((user.getUsername().toLowerCase().trim()))) {
				redirectAttributes.addFlashAttribute("data", "No tiene permisos sobre el rfc.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}
			*/
			if(requestEdit.getTypePetition().getCode().equals("RM-P1-R4")) {
				model.addAttribute("request", requestEdit);
				model.addAttribute("systems", systems);
				model.addAttribute("ambients", ambientService.list("", requestEdit.getSystemInfo().getCode()));
				return "/request/editRequestR4";
			}
			
			if(requestEdit.getTypePetition().getCode().equals("RM-P1-R5")) {
				model.addAttribute("request", requestEdit);
				model.addAttribute("systems", systems);
				RequestRM_P1_R5 requestR5= requestServiceRm5.requestRm5(requestEdit.getId());
				model.addAttribute("requestR5", requestR5);
				model.addAttribute("ambients", ambientService.list("", requestEdit.getSystemInfo().getCode()));
				return "/request/editRequestR5";
			}
			
		


			return "/rfc/editRFC";

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/";
	}
	

	@RequestMapping(value = { "/listUser/{id}" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet<RequestRM_P1_R4> changeProject(@PathVariable Long id, Locale locale, Model model) {
		JsonSheet<RequestRM_P1_R4> requestsRM = new JsonSheet<>();
		try {
			requestsRM.setData( requestServiceRm4.listRequestRm4(id));
		} catch (Exception e) {
			Sentry.capture(e, "requestUser");

			e.printStackTrace();
		}

		return requestsRM;
	}

	@RequestMapping(path = "/addUser", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveUser(HttpServletRequest request, @RequestBody RequestRM_P1_R4 userRequestAdd) {
		JsonResponse res = new JsonResponse();
		try {
		
				userRequestAdd.setAmbient(ambientService.findById(userRequestAdd.getAmbientId()));
				userRequestAdd.setRequestBase(requestBaseService.findById(userRequestAdd.getRequestBaseId()));
				requestServiceRm4.save(userRequestAdd);
				res.setStatus("success");
				res.setMessage("Se guardo correctamente el usuario!");
	
		} catch (Exception e) {
			Sentry.capture(e, "usuario");
			res.setStatus("exception");
			res.setMessage("Error al guardar usuario!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	@RequestMapping(value = "/deleteUser/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteUserRM4(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			requestServiceRm4.delete(id);
			res.setMessage("Usuario eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "rm4");
			res.setStatus("exception");
			res.setMessage("Error al eliminar el usuario!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	@RequestMapping(value = "/tiny/{id}", method = RequestMethod.GET)
	public String indexSumm(@PathVariable Long id,HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		RequestBase requestEdit = new RequestBase();
		try {
			if (id == null) {
				return "redirect:/";
			}

			requestEdit = requestBaseService.findById(id);

			if (requestEdit == null) {
				return "/plantilla/404";
			}

			if (!requestEdit.getStatus().getName().equals("Borrador")) {
				redirectAttributes.addFlashAttribute("data", "Solicitud no disponible para editar.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}
			//Integer idManager= getUserLogin().getId();
			/*
			Integer countByManager= requestBaseService.countByManager(idManager, requestEdit.getId());
			if(countByManager==0) {
				redirectAttributes.addFlashAttribute("data", "No tiene permisos sobre la  ya que no formas parte de este equipo.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}*/
			
			
/*	
			if (!(rfcEdit.getUser().getUsername().toLowerCase().trim())
					.equals((user.getUsername().toLowerCase().trim()))) {
				redirectAttributes.addFlashAttribute("data", "No tiene permisos sobre el rfc.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}
			*/
			if(requestEdit.getTypePetition().getCode().equals("RM-P1-R4")) {
				model.addAttribute("request", requestEdit);
				List<RequestRM_P1_R4> listUser=requestServiceRm4.listRequestRm4(requestEdit.getId());
				model.addAttribute("listUsers", listUser);
				model.addAttribute("ambients", ambientService.list("", requestEdit.getSystemInfo().getCode()));
				return "/request/sectionsEditR4/tinySummaryRequest";
			}
		}catch (Exception e) {
			Sentry.capture(e, "requestSummary");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/";
		}
		return "redirect:/";

	}
	
	@SuppressWarnings("null")
	@RequestMapping(value = "/saveRequest", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse saveRelease(HttpServletRequest request, @RequestBody RequestBase addRequest) {
		User user=userService.getUserByUsername(getUserLogin().getUsername());
		JsonResponse res = new JsonResponse();
		ArrayList<MyError> errors = new ArrayList<MyError>();

		try {
			RequestBase requestMod = requestBaseService.findById(addRequest.getId());
			addRequest.setTypePetition(requestMod.getTypePetition());
			errors = validSections(addRequest, errors);
			
			addRequest.setUser(requestMod.getUser());
			addRequest.setNumRequest(requestMod.getNumRequest());
			addRequest.setCodeProyect(requestMod.getCodeProyect());
			addRequest.setSiges(requestMod.getSiges());
			addRequest.setOperator(user.getFullName());
			addRequest.setStatus(requestMod.getStatus());
			addRequest.setUser(requestMod.getUser());
			addRequest.setRequestDate(requestMod.getRequestDate());
			
			addRequest.setSystemInfo(requestMod.getSystemInfo());
			requestBaseService.update(addRequest);
			res.setStatus("success");
			if (errors.size() > 0) {
				// Se adjunta lista de errores
				res.setStatus("fail");
				res.setErrors(errors);
			}

		} catch (Exception e) {
			Sentry.capture(e, "request");
			res.setStatus("exception");
			res.setException("Error al guardar la solicitud: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	@RequestMapping(value = "/updateRequest/{id}", method = RequestMethod.GET)
	public String updateRFC(@PathVariable Long id, HttpServletRequest request, Locale locale, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			RequestBase requestBase = new RequestBase();
			requestBase = requestBaseService.findById(id);
			//Si la solicitud no existe se regresa al inicio.
			if (request == null) {
				return "redirect:/homeRequest";
			}
			// Verificar si existe un flujo para el sistema

			StatusRequest status = statusService.findByKey("name", "Solicitado");

//			if (node != null)
			
//				release.setNode(node);

			requestBase.setStatus(status);
			requestBase.setMotive(status.getReason());
			requestBase.setRequestDate((CommonUtils.getSystemTimestamp()));
			
			requestBase.setOperator(getUserLogin().getFullName());
			 TypePetition typePettion=requestBase.getTypePetition();
			if (Boolean.valueOf(parameterService.getParameterByCode(1).getParamValue())) {
				if (typePettion.getEmailTemplate() != null) {
					EmailTemplate email = typePettion.getEmailTemplate();
					RequestBase requestEmail = requestBase;
					Thread newThread = new Thread(() -> {
						try {
							emailService.sendMailRequestR4(requestEmail, email);
						} catch (Exception e) {
							Sentry.capture(e, "request");
						}

					});
					newThread.start();
				}
			}
	

			requestBaseService.update(requestBase);

			
			return "redirect:/request/summaryRequest-" + requestBase.getId();
		} catch (Exception e) {
			Sentry.capture(e, "request");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/homeRequest";
	}
	
	@RequestMapping(value = "/summaryRequest-{id}", method = RequestMethod.GET)
	public String summmary(@PathVariable Long id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) throws SQLException {
		User user =  userService.getUserByUsername(getUserLogin().getUsername());
		List<System> systems = systemService.listProjects(user.getId());
		RequestBase requestEdit = new RequestBase();
		try {
			if (id == null) {
				return "redirect:/";
			}

			requestEdit = requestBaseService.findById(id);

			if (requestEdit == null) {
				return "/plantilla/404";
			}


			if(requestEdit.getTypePetition().getCode().equals("RM-P1-R4")) {
				model.addAttribute("request", requestEdit);
				List<RequestRM_P1_R4> listUser=requestServiceRm4.listRequestRm4(requestEdit.getId());
				model.addAttribute("listUsers", listUser);
				model.addAttribute("statuses", statusService.findAll());
				model.addAttribute("ambients", ambientService.list("", requestEdit.getSystemInfo().getCode()));
				return "/request/sectionsEditR4/summaryRequest";
			}
			

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina resumen de la solicitud." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/";
		}

		return "/rfc/summaryRFC";
	}
	
	public ArrayList<MyError> validSections(RequestBase request, ArrayList<MyError> errors) {
		if(request.getTypePetition().getCode().equals("RM-P1-R4")) {
			List<RequestRM_P1_R4> listUser=requestServiceRm4.listRequestRm4(request.getId());
			if(listUser.size()==0) {
				errors.add(new MyError("requiredUser","Se requiere al menos un usuario"));
			}
			
			
		}

		return errors;
	}
	
	public void loadCountsRelease(HttpServletRequest request, Integer id) {
		//PUser userLogin = getUserLogin();
		//List<PSystem> systems = systemService.listProjects(userLogin.getId());
		Map<String, Integer> userC = new HashMap<String, Integer>();
		userC.put("draft", requestBaseService.countByType(id, "Borrador", 1, null));
		userC.put("requested", requestBaseService.countByType(id, "Solicitado", 1, null));
		userC.put("completed", requestBaseService.countByType(id, "Completado", 1, null));
		userC.put("all", (userC.get("draft") + userC.get("requested") + userC.get("completed")));
		request.setAttribute("userC", userC);
		
	}
	
}

package com.soin.sgrm.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gdata.client.appsforyourdomain.UserService;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.Release_RFC;
import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.RequestRM_P1_R4;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.StatusRequest;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.model.User;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.AmbientService;
import com.soin.sgrm.service.RequestBaseService;
import com.soin.sgrm.service.RequestRM_P1_R4Service;
import com.soin.sgrm.service.SigesService;
import com.soin.sgrm.service.StatusRequestService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.TypePetitionService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
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
	AmbientService ambientService;
	
	
	
	public static final Logger logger = Logger.getLogger(RFCController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			Integer userLogin = getUserLogin().getId();
			//loadCountsRelease(request, userLogin);
			List<System> systems = systemService.listProjects(getUserLogin().getId());
			//List<Priority> priorities = priorityService.list();
			List<StatusRequest> statuses = statusService.findAll();
			List<TypePetition> typePetitions=typePetitionService.findAll();
			//List<Impact> impacts = impactService.list();
			//model.addAttribute("priorities", priorities);
			//model.addAttribute("impacts", impacts);
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
}

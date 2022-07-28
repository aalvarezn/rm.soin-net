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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.Release_RFC;
import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.StatusRequest;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.AmbientService;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.ParameterService;
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
@RequestMapping("/management/request")
public class RequestBaseManagementController extends BaseController {

	public static final Logger logger = Logger.getLogger(RequestBaseManagementController.class);
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
	
	@Autowired
	EmailTemplateService emailService;
	
	@Autowired
	ParameterService parameterService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			Integer userLogin = getUserLogin().getId();
			//loadCountsRelease(request, userLogin);
			List<System> systems = systemService.list();
			List<StatusRequest> statuses = statusService.findAll();
			List<TypePetition> typePetitions=typePetitionService.findAll();
			model.addAttribute("statuses", statuses);
			model.addAttribute("typePetitions",typePetitions);
			model.addAttribute("systems", systems);
		} catch (Exception e) {
			Sentry.capture(e, "request");
			e.printStackTrace();
		}
		return "/request/requestManagement";
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

			requests = requestBaseService.findAllRequest(sEcho, iDisplayStart, iDisplayLength, sSearch, statusId, dateRange,systemId,typePetitionId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return requests;
	}
	
	@RequestMapping(value = "/statusRequest", method = RequestMethod.GET)
	public @ResponseBody JsonResponse draftRFC(HttpServletRequest request, Model model,
			@RequestParam(value = "idRequest", required = true) Long idRequest,
			@RequestParam(value = "idStatus", required = true) Long idStatus,
			@RequestParam(value = "dateChange", required = false) String dateChange,
			@RequestParam(value = "motive", required = true) String motive) {
		JsonResponse res = new JsonResponse();
		try {
			RequestBase requestBase = requestBaseService.findById(idRequest);
			StatusRequest status = statusService.findById(idStatus);
			String user=getUserLogin().getFullName();

			requestBase.setStatus(status);
			requestBase.setOperator(getUserLogin().getFullName());
			requestBase.setRequestDate((CommonUtils.getSystemTimestamp()));
			
			requestBase.setMotive(motive);
			requestBaseService.update(requestBase);
			res.setStatus("success");

		} catch (Exception e) {
			Sentry.capture(e, "requestManagement");
			res.setStatus("exception");
			res.setException("Error al cambiar estado de la solictud: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	@RequestMapping(value = "/deleteRequest/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteRequest(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			RequestBase requestBase = requestBaseService.findById(id);
			if (requestBase.getStatus().getName().equals("Borrador")) {
				
					StatusRequest status = statusService.findByKey("name", "Anulado");
					requestBase.setStatus(status);
					requestBase.setMotive(status.getReason());
					requestBase.setOperator(getUserLogin().getFullName());
					requestBaseService.update(requestBase);
				
			} else {
				res.setStatus("fail");
				res.setException("La acción no se pudo completar, la solicitud no esta en estado de Borrador.");
			}
		} catch (Exception e) {
			Sentry.capture(e, "release");
			res.setStatus("exception");
			res.setException("La acción no se pudo completar correctamente.");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	@RequestMapping(value = "/cancelRequest", method = RequestMethod.GET)
	public @ResponseBody JsonResponse cancelRelease(HttpServletRequest request, Model model,
			@RequestParam(value = "idRequest", required = true) Long idRequest) {
		JsonResponse res = new JsonResponse();
		try {
			RequestBase requestBase = requestBaseService.findById(idRequest);
			StatusRequest status = statusService.findByKey("name","Anulado");
			requestBase.setStatus(status);
			requestBase.setOperator(getUserLogin().getFullName());
			requestBase.setMotive(status.getReason());
			requestBaseService.update(requestBase);
			res.setStatus("success");

		}  catch (Exception e) {
			Sentry.capture(e, "requestManagement");
			res.setStatus("exception");
			res.setException("Error al cancelar la solicitud: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
}

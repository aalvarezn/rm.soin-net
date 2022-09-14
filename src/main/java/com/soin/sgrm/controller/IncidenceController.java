package com.soin.sgrm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.StatusIncidence;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.TypeIncidence;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.EmailReadService;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.ImpactService;
import com.soin.sgrm.service.IncidenceService;
import com.soin.sgrm.service.ParameterService;
import com.soin.sgrm.service.PriorityService;
import com.soin.sgrm.service.RFCService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.SigesService;
import com.soin.sgrm.service.StatusIncidenceService;
import com.soin.sgrm.service.StatusRFCService;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.TreeService;
import com.soin.sgrm.service.TypeChangeService;
import com.soin.sgrm.service.TypeIncidenceService;

@Controller
@RequestMapping(value = "/incidence")
public class IncidenceController extends BaseController {
	@Autowired
	PriorityService priorityService;

	@Autowired
	RFCService rfcService;

	@Autowired
	StatusIncidenceService statusService;
	
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
	TypeIncidenceService priorityIncidenceService;
	
	@Autowired
	IncidenceService incidenceService;
	
	@Autowired
	EmailReadService emailReadService;
	
	@Autowired
	com.soin.sgrm.service.UserService userService;
	
	
	public static final Logger logger = Logger.getLogger(IncidenceController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			Integer userLogin = getUserLogin().getId();
			loadCountsRelease(request, userLogin);
			List<TypeIncidence> priorities = priorityIncidenceService.findAll();
			List<StatusIncidence> statuses = statusService.findAll();
			model.addAttribute("priorities", priorities);
			model.addAttribute("statuses", statuses);
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

			incidences = incidenceService.findAllRequest(name, sEcho, iDisplayStart, iDisplayLength, sSearch, statusId, dateRange, systemId, statusId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return incidences;
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
	
	@RequestMapping(value = "/readEmail", method = RequestMethod.GET)
	public String readEmails(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			emailReadService.emailRead();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "se leyo correctamente";

	}

}

package com.soin.sgrm.controller;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Errors;
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.ReleaseError;
import com.soin.sgrm.model.Release_RFC;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.User;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.ErrorService;
import com.soin.sgrm.service.ImpactService;
import com.soin.sgrm.service.PriorityService;
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.service.RFCService;
import com.soin.sgrm.service.ReleaseErrorService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.StatusRFCService;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/management/error")
public class ErrorsManagementController extends BaseController{
	public static final Logger logger = Logger.getLogger(ErrorsManagementController.class);
	@Autowired
	RFCService rfcService;
	@Autowired 
	StatusRFCService statusService;
	
	@Autowired 
	StatusService statusReleaseService;
	
	@Autowired 
	ReleaseService releaseService;
	
	@Autowired
	SystemService systemService;
	
	@Autowired
	PriorityService priorityService;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	ImpactService impactService;
	
	@Autowired
	ErrorService errorService;
	
	@Autowired
	ReleaseErrorService releaseErrorService;
	
	@Autowired
	com.soin.sgrm.service.UserService userService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			User userLogin = userService.getUserByUsername(getUserLogin().getUsername());
			//loadCountsRFC(request, userLogin.getId());
			List<System> systems = systemService.list();
			List<Project> projects = projectService.listAll();
			List<Errors> errors = errorService.findAll();
			model.addAttribute("projects", projects);
			model.addAttribute("errors", errors);
			model.addAttribute("systems", systems);
		} catch (Exception e) {
			Sentry.capture(e, "errors");
			e.printStackTrace();
		}
		return "/errors/errorsManagement";

	}
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<ReleaseError> releaseError = new JsonSheet<>();
		try {

			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			String sSearch = request.getParameter("sSearch");
			 Long errorId;
			 int projectId;
			 int systemId;
			if (request.getParameter("errorId").equals("")) {
				errorId = null;
			} else {
				errorId = (long) Integer.parseInt(request.getParameter("errorId"));
			}
			
			if (request.getParameter("systemId").equals("")) {
				systemId = 0;
			} else {
				systemId = Integer.parseInt(request.getParameter("systemId"));
			}
			
			if (request.getParameter("projectId").equals("")) {
				projectId = 0;
			} else {
				projectId = Integer.parseInt(request.getParameter("projectId"));
			}
			String dateRange = request.getParameter("dateRange");

			releaseError = releaseErrorService.findAll(sEcho, iDisplayStart, iDisplayLength, sSearch, errorId, dateRange,projectId, systemId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return releaseError;
	}
	
		
}

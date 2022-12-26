package com.soin.sgrm.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Errors_RFC;
import com.soin.sgrm.model.Errors_Release;
import com.soin.sgrm.model.Errors_Requests;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.RFCError;
import com.soin.sgrm.model.ReleaseError;
import com.soin.sgrm.model.RequestError;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.model.User;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.ErrorRFCService;
import com.soin.sgrm.service.ErrorReleaseService;
import com.soin.sgrm.service.ErrorRequestService;
import com.soin.sgrm.service.ImpactService;
import com.soin.sgrm.service.PriorityService;
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.service.RFCErrorService;
import com.soin.sgrm.service.RFCService;
import com.soin.sgrm.service.ReleaseErrorService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.RequestErrorService;
import com.soin.sgrm.service.SigesService;
import com.soin.sgrm.service.StatusRFCService;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.TypePetitionService;
import com.soin.sgrm.utils.JsonResponse;
import com.sun.mail.iap.ByteArray;


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
	ErrorReleaseService errorReleaseService;
	
	@Autowired
	ErrorRFCService errorRFCService;
	
	@Autowired
	ErrorRequestService errorRequestService;
	
	@Autowired
	ReleaseErrorService releaseErrorService;
	
	@Autowired
	RFCErrorService rfcErrorService;
	
	@Autowired
	RequestErrorService requestErrorService;
	
	@Autowired
	SigesService sigesService;
	
	@Autowired
	TypePetitionService typePetitionService;
	
	@Autowired
	com.soin.sgrm.service.UserService userService;
	
	@Autowired
	private Environment env;
	
	@RequestMapping(value = "/release", method = RequestMethod.GET)
	public String indexReleaseError(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			User userLogin = userService.getUserByUsername(getUserLogin().getUsername());
			//loadCountsRFC(request, userLogin.getId());
			List<System> systems = systemService.list();
			List<Project> projects = projectService.listAll();
			List<Errors_Release> errors = errorReleaseService.findAll();
			model.addAttribute("projects", projects);
			model.addAttribute("errors", errors);
			model.addAttribute("systems", systems);
		} catch (Exception e) {
			Sentry.capture(e, "errors");
			e.printStackTrace();
		}
		return "/errors/errorsReleaseManagement";

	}
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/release/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet listReleaseError(HttpServletRequest request, Locale locale, Model model) {
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
	
	@RequestMapping(value = "/rfc", method = RequestMethod.GET)
	public String indexRFCError(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			User userLogin = userService.getUserByUsername(getUserLogin().getUsername());
			//loadCountsRFC(request, userLogin.getId());
			List<System> systems = systemService.list();
			List<Siges> siges = sigesService.findAll();
			List<Errors_RFC> errors = errorRFCService.findAll();
			model.addAttribute("siges", siges);
			model.addAttribute("errors", errors);
			model.addAttribute("systems", systems);
		} catch (Exception e) {
			Sentry.capture(e, "errors");
			e.printStackTrace();
		}
		return "/errors/errorsRFCManagement";

	}
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/rfc/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet listRFCError(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<RFCError> rfcError = new JsonSheet<>();
		try {

			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			String sSearch = request.getParameter("sSearch");
			 Long errorId;
			 Long sigesId;
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
			
			if (request.getParameter("sigesId").equals("")) {
				sigesId = null;
			} else {
				sigesId = (long)Integer.parseInt(request.getParameter("sigesId"));
			}
			String dateRange = request.getParameter("dateRange");

			rfcError = rfcErrorService.findAll(sEcho, iDisplayStart, iDisplayLength, sSearch, errorId, dateRange,sigesId, systemId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rfcError;
	}
	
	@RequestMapping(value = "/request", method = RequestMethod.GET)
	public String indexRequestError(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			User userLogin = userService.getUserByUsername(getUserLogin().getUsername());
			//loadCountsRFC(request, userLogin.getId());
			List<System> systems = systemService.list();
			List<TypePetition> typePetitions = typePetitionService.findAll();
			List<Errors_Requests> errors = errorRequestService.findAll();
			model.addAttribute("typePetitions", typePetitions);
			model.addAttribute("errors", errors);
			model.addAttribute("systems", systems);
		} catch (Exception e) {
			Sentry.capture(e, "errors");
			e.printStackTrace();
		}
		return "/errors/errorsRequestManagement";

	}
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/request/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet listRequestError(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<RequestError> requestError = new JsonSheet<>();
		try {

			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			String sSearch = request.getParameter("sSearch");
			 Long errorId;
			 Long typePetitionId;
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
			
			if (request.getParameter("typePetitionId").equals("")) {
				typePetitionId = null;
			} else {
				typePetitionId = (long)Integer.parseInt(request.getParameter("typePetitionId"));
			}
			String dateRange = request.getParameter("dateRange");

			requestError = requestErrorService.findAll(sEcho, iDisplayStart, iDisplayLength, sSearch, errorId, dateRange,typePetitionId, systemId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return requestError;
	}

		
}
package com.soin.sgrm.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/management/incidence")
public class IncidenceManagementController extends BaseController {
	
	public static final Logger logger = Logger.getLogger(IncidenceManagementController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			/*
			User userLogin = userService.getUserByUsername(getUserLogin().getUsername());
			loadCountsRFC(request, userLogin.getId());
			List<System> systems = systemService.list();
			List<Priority> priorities = priorityService.list();
			List<StatusRFC> statuses = statusService.findAll();
			List<Impact> impacts = impactService.list();
			model.addAttribute("priorities", priorities);
			model.addAttribute("impacts", impacts);
			model.addAttribute("statuses", statuses);
			model.addAttribute("systems", systems);
			*/
		} catch (Exception e) {
			Sentry.capture(e, "incidenceManagement");
			e.printStackTrace();
		}
		return "/incindence/incidenceManagement";

	}

}

package com.soin.sgrm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.System;

@Controller
@RequestMapping(value = "/petition")
public class PetitionController extends BaseController{

	
	public static final Logger logger = Logger.getLogger(PetitionController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			Integer userLogin = getUserLogin().getId();
			loadCountsRequest(request, userLogin);
			/*
			List<System> systems = systemService.listProjects(getUserLogin().getId());
			List<Priority> priorities = priorityService.list();
			List<StatusRFC> statuses = statusService.findAll();
			List<Impact> impacts = impactService.list();
			
			model.addAttribute("priorities", priorities);
			model.addAttribute("impacts", impacts);
			model.addAttribute("statuses", statuses);
			model.addAttribute("systems", systems);
		*/
		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			e.printStackTrace();
		}
		return "/rfc/rfc";

	}

	public void loadCountsRequest(HttpServletRequest request, Integer id) {
		//PUser userLogin = getUserLogin();
		//List<PSystem> systems = systemService.listProjects(userLogin.getId());
		Map<String, Integer> userC = new HashMap<String, Integer>();
		/*
		userC.put("draft", rfcService.countByType(id, "Borrador", 1, null));
		userC.put("requested", rfcService.countByType(id, "Solicitado", 1, null));
		userC.put("completed", rfcService.countByType(id, "Completado", 1, null));
		userC.put("all", (userC.get("draft") + userC.get("requested") + userC.get("completed")));
		*/
		request.setAttribute("userC", userC);
		
	}
}

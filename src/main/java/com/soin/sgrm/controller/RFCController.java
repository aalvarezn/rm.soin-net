package com.soin.sgrm.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.SystemUser;
import com.soin.sgrm.model.pos.PPriority;
import com.soin.sgrm.service.pos.PriorityService;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping(value = "/rfc")
public class RFCController extends BaseController {

	@Autowired
	PriorityService priority;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			String name = getUserLogin().getUsername();

			List<PPriority> priorities = priority.findAll();
			model.addAttribute("priorities", priorities);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/rfc/rfc";

	}

}

package com.soin.sgrm.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.SystemUser;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.model.pos.PPriority;
import com.soin.sgrm.model.pos.PRFC;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.security.UserLogin;
import com.soin.sgrm.service.pos.PriorityService;
import com.soin.sgrm.service.pos.RFCService;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping(value = "/rfc")
public class RFCController extends BaseController {

	@Autowired
	PriorityService priority;

	@Autowired
	RFCService rfc;

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

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		UserLogin user = getUserLogin();

		Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
		Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
		Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
		Integer sStatus = Integer.parseInt(request.getParameter("sEcho"));

		String sSearch = request.getParameter("sStatus");
		String dateRange = request.getParameter("dateRange");

		JsonSheet<PRFC> rfcs = rfc.findAll(sEcho, iDisplayStart, iDisplayLength, sSearch, sStatus, dateRange);
		return rfcs;
	}

}

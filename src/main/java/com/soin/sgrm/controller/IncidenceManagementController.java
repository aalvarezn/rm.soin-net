package com.soin.sgrm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.PriorityIncidence;
import com.soin.sgrm.model.StatusIncidence;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.TypeIncidence;
import com.soin.sgrm.model.User;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.EmailReadService;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.IncidenceService;
import com.soin.sgrm.service.ParameterService;
import com.soin.sgrm.service.PriorityIncidenceService;
import com.soin.sgrm.service.RFCService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.SigesService;
import com.soin.sgrm.service.StatusIncidenceService;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.TreeService;
import com.soin.sgrm.service.TypeChangeService;
import com.soin.sgrm.service.TypeIncidenceService;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/management/incidence")
public class IncidenceManagementController extends BaseController {
	
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
	TypeIncidenceService typeIncidenceService;

	@Autowired
	IncidenceService incidenceService;

	@Autowired
	PriorityIncidenceService priorityIncidenceService;

	@Autowired
	EmailReadService emailReadService;

	@Autowired
	com.soin.sgrm.service.UserService userService;

	public static final Logger logger = Logger.getLogger(IncidenceManagementController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
			Integer userLogin = getUserLogin().getId();
			loadCountsRelease(request, userLogin);
			List<TypeIncidence> typeIncidences = typeIncidenceService.findAll();
			List<StatusIncidence> statuses = statusService.findAll();
			List<PriorityIncidence> priorities = priorityIncidenceService.findAll();
			model.addAttribute("typeincidences", typeIncidences);
			model.addAttribute("priorities", priorities);
			model.addAttribute("statuses", statuses);
		} catch (Exception e) {
			Sentry.capture(e, "incidence");
			e.printStackTrace();
		}
		return "/incidence/incidenceManagement";

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
			Long priorityId;
			Long typeId;
			if (request.getParameter("statusId").equals("")) {
				statusId = null;
			} else {
				statusId = (long) Integer.parseInt(request.getParameter("statusId"));
			}
			if (request.getParameter("priorityId").equals("")) {
				priorityId = null;
			} else {
				priorityId = (long) Integer.parseInt(request.getParameter("priorityId"));
			}

			if (request.getParameter("typeId").equals("")) {
				typeId = null;
			} else {
				typeId = (long) Integer.parseInt(request.getParameter("typeId"));
			}
			String dateRange = request.getParameter("dateRange");

			incidences = incidenceService.findAllRequest(sEcho, iDisplayStart, iDisplayLength, sSearch, statusId,
					dateRange, typeId, priorityId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return incidences;
	}

	@RequestMapping(value = "/editIncidence-{id}", method = RequestMethod.GET)
	public String editIncidence(@PathVariable Long id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		Incidence incidenceEdit = new Incidence();
		User user = userService.getUserByUsername(getUserLogin().getUsername());
		List<System> systems = systemService.listProjects(user.getId());
		try {
			if (id == null) {
				return "redirect:/";
			}

			incidenceEdit = incidenceService.getIncidences(id);

			if (incidenceEdit == null) {
				return "/plantilla/404";
			}

			if (!incidenceEdit.getStatus().getName().equals("Borrador")) {
				redirectAttributes.addFlashAttribute("data", "RFC no disponible para editar.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}
			Collection<? extends GrantedAuthority> roles = getUserLogin().getAuthorities();
			Integer countRol = 0;
			for (GrantedAuthority rol : roles) {
				if (rol.getAuthority().equals("ROLE_Admin")) {
					countRol++;
				}
				if (rol.getAuthority().equals("ROLE_Gestor Incidencias")) {
					countRol++;
				}

			}

			if (countRol == 0) {
				redirectAttributes.addFlashAttribute("data",
						"No tiene permisos sobre la incidencia ya que no formas parte de este equipo.");
				String referer = request.getHeader("Referer");
				return "redirect:" + referer;
			}
			/*
			 * if (!(rfcEdit.getUser().getUsername().toLowerCase().trim())
			 * .equals((user.getUsername().toLowerCase().trim()))) {
			 * redirectAttributes.addFlashAttribute("data",
			 * "No tiene permisos sobre el rfc."); String referer =
			 * request.getHeader("Referer"); return "redirect:" + referer; }
			 */

			model.addAttribute("systems", systems);
			model.addAttribute("typeChange", typeChangeService.findAll());
			model.addAttribute("priorities", priorityIncidenceService.findAll());
			model.addAttribute("typeincidences", typeIncidenceService.findAll());
			model.addAttribute("incidence", incidenceEdit);
			model.addAttribute("senders", incidenceEdit.getSenders());
			model.addAttribute("message", incidenceEdit.getMessage());
			if(incidenceEdit.getTypeIncidence()!=null) {
			//model.addAttribute("ccs", getCC(incidenceEdit.getTypeIncidence().getEmailTemplate().getCc()));
			}
   			return "/incidence/editIncidence";

		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			redirectAttributes.addFlashAttribute("data", e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}

		return "redirect:/";
	}

	public List<String> getCC(String ccs) {

		List<String> getCC = new ArrayList<String>();
		if (ccs != null) {
			ccs.split(",");
			for (String cc : ccs.split(",")) {
				getCC.add(cc);
			}
		}
		return getCC;
	}

	public void loadCountsRelease(HttpServletRequest request, Integer id) {
		// PUser userLogin = getUserLogin();
		// List<PSystem> systems = systemService.listProjects(userLogin.getId());
		Map<String, Integer> userC = new HashMap<String, Integer>();
		userC.put("draft", rfcService.countByType(id, "Borrador", 1, null));
		userC.put("requested", rfcService.countByType(id, "Solicitado", 1, null));
		userC.put("completed", rfcService.countByType(id, "Completado", 1, null));
		userC.put("all", (userC.get("draft") + userC.get("requested") + userC.get("completed")));
		request.setAttribute("userC", userC);

	}

	@RequestMapping(value = { "/readEmail" }, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public String readEmails() {
		try {
			emailReadService.emailRead();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "se leyo correctamente";

	}


}

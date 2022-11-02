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
import com.soin.sgrm.model.AttentionGroup;
import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.PriorityIncidence;
import com.soin.sgrm.model.StatusIncidence;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.TypeIncidence;
import com.soin.sgrm.model.User;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.AttentionGroupService;
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
import com.soin.sgrm.utils.CommonUtils;
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

	@Autowired
	AttentionGroupService attentionGroupService;

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
			List<AttentionGroup> attentionGroup = attentionGroupService.findGroupByUserId(userLogin);
			model.addAttribute("typeincidences", typeIncidences);
			model.addAttribute("attentionGroup", attentionGroup);
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
	public String editIncidence(@PathVariable String id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		Incidence incidenceEdit = new Incidence();
		User user = userService.getUserByUsername(getUserLogin().getUsername());
		List<System> systems = systemService.listProjects(user.getId());
		List<AttentionGroup> attentionGroup = attentionGroupService.findGroupByUserId(getUserLogin().getId());
		try {
			model.addAttribute("parameter", id);
			model.addAttribute("systems", systems);
			model.addAttribute("typeChange", typeChangeService.findAll());
			model.addAttribute("priorities", priorityIncidenceService.findAll());
			model.addAttribute("typeincidences", typeIncidenceService.findAll());
			model.addAttribute("incidence", incidenceEdit);
			model.addAttribute("senders", incidenceEdit.getSenders());
			model.addAttribute("message", incidenceEdit.getMessage());
	
			if (CommonUtils.isNumeric(id)) {
				incidenceEdit = incidenceService.getIncidences((Long.parseLong(id)));
			}

			if (incidenceEdit == null) {
				return "redirect:/";
			}
			if (incidenceEdit.getStatus().getName().equals("Enviado a RM")) {
				boolean verify=false;
				for(AttentionGroup attentionGroupName: attentionGroup) {
					if(attentionGroupName.getCode().equals("RM")){
						verify=true;
					}
				}
				if(!verify) {
					redirectAttributes.addFlashAttribute("data", "Esta incidencia no disponible para editar ya que no eres parte de este grupo.");
					String referer = request.getHeader("Referer");
					return "redirect:" + referer;
				}
				
			}
			
			if (incidenceEdit.getStatus().getName().equals("Enviado a INFRA")) {
				boolean verify=false;
				for(AttentionGroup attentionGroupName: attentionGroup) {
					if(attentionGroupName.getCode().equals("INFRA")){
						verify=true;
					}
				}
				if(!verify) {
					redirectAttributes.addFlashAttribute("data", "Esta incidencia no disponible para editar ya que no eres parte de este grupo.");
					String referer = request.getHeader("Referer");
					return "redirect:" + referer;
				}
				
			}
			
			if (incidenceEdit.getStatus().getName().equals("Enviado a LABS")) {
				boolean verify=false;
				for(AttentionGroup attentionGroupName: attentionGroup) {
					if(attentionGroupName.getCode().equals("LABS")){
						verify=true;
					}
				}
				if(!verify) {
					redirectAttributes.addFlashAttribute("data", "Esta incidencia no disponible para editar ya que no eres parte de este grupo.");
					String referer = request.getHeader("Referer");
					return "redirect:" + referer;
				}
				
			}
			
			if (incidenceEdit.getStatus().getName().equals("Solicitado")) {
				boolean verify=false;
				for(AttentionGroup attentionGroupName: attentionGroup) {
					if(attentionGroupName.getCode().equals("GI")){
						verify=true;
					}
				}
				if(!verify) {
					redirectAttributes.addFlashAttribute("data", "Esta incidencia no disponible para editar ya que no eres parte de este grupo.");
					String referer = request.getHeader("Referer");
					return "redirect:" + referer;
				}
				
			}

			

			model.addAttribute("incidence", incidenceEdit);
			return "/incidence/summaryIncidence";

		} catch (Exception e) {
			Sentry.capture(e, "incidence");
			redirectAttributes.addFlashAttribute("data",
					"Error en la carga de la pagina resumen incidence." + " ERROR: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return "redirect:/homeIncidence";
		}

		


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

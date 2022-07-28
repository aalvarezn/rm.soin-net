package com.soin.sgrm.controller;

import java.sql.SQLException;
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
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.Release_RFC;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.User;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.ImpactService;
import com.soin.sgrm.service.PriorityService;
import com.soin.sgrm.service.RFCService;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.StatusRFCService;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/management/rfc")
public class RFCManagementController extends BaseController{
	public static final Logger logger = Logger.getLogger(RFCManagementController.class);
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
	ImpactService impactService;
	
	@Autowired
	com.soin.sgrm.service.UserService userService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session,
			RedirectAttributes redirectAttributes) {
		try {
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
		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			e.printStackTrace();
		}
		return "/rfc/rfcManagement";

	}
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<RFC> rfcs = new JsonSheet<>();
		try {

			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			String sSearch = request.getParameter("sSearch");
			 Long statusId;
			 int priorityId=0;
			 int systemId;
			if (request.getParameter("statusId").equals("")) {
				statusId = null;
			} else {
				statusId = (long) Integer.parseInt(request.getParameter("statusId"));
			}
			
			if (request.getParameter("systemId").equals("")) {
				systemId = 0;
			} else {
				systemId = Integer.parseInt(request.getParameter("systemId"));
			}
			String dateRange = request.getParameter("dateRange");

			rfcs = rfcService.findAll1(sEcho, iDisplayStart, iDisplayLength, sSearch, statusId, dateRange,priorityId, systemId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rfcs;
	}
	@RequestMapping(value = "/cancelRFC", method = RequestMethod.GET)
	public @ResponseBody JsonResponse cancelRelease(HttpServletRequest request, Model model,
			@RequestParam(value = "idRFC", required = true) Long idRFC) {
		JsonResponse res = new JsonResponse();
		try {
			RFC rfc = rfcService.findById(idRFC);
			StatusRFC status = statusService.findByKey("name","Anulado");
			rfc.setStatus(status);
			rfc.setOperator(getUserLogin().getFullName());
			rfc.setMotive(status.getReason());
			rfcService.update(rfc);
			res.setStatus("success");

		}  catch (Exception e) {
			Sentry.capture(e, "rfcManagement");
			res.setStatus("exception");
			res.setException("Error al cancelar el rfc: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	@RequestMapping(value = "/statusRFC", method = RequestMethod.GET)
	public @ResponseBody JsonResponse draftRFC(HttpServletRequest request, Model model,
			@RequestParam(value = "idRFC", required = true) Long idRFC,
			@RequestParam(value = "idStatus", required = true) Long idStatus,
			@RequestParam(value = "dateChange", required = false) String dateChange,
			@RequestParam(value = "motive", required = true) String motive) {
		JsonResponse res = new JsonResponse();
		try {
			RFC rfc = rfcService.findById(idRFC);
			StatusRFC status = statusService.findById(idStatus);
			String user=getUserLogin().getFullName();
			if (status != null && status.getName().equals("Borrador")) {
				Set<Release_RFC> releases=rfc.getReleases();
				for(Release_RFC release: releases) {
					release.setStatus(release.getStatusBefore());
					release.setMotive("Devuelto al estado "+release.getStatus().getName());
					releaseService.updateStatusReleaseRFC(release,user);
				}
				
				/*if (release.getStatus().getId() != status.getId())
					release.setRetries(release.getRetries() + 1);*/
			}
			rfc.setStatus(status);
			rfc.setOperator(getUserLogin().getFullName());
			rfc.setRequestDate((CommonUtils.getSystemTimestamp()));
			
			rfc.setMotive(motive);
			rfcService.update(rfc);
			res.setStatus("success");

		} catch (Exception e) {
			Sentry.capture(e, "rfcManagement");
			res.setStatus("exception");
			res.setException("Error al cambiar estado del RFC: " + e.getMessage());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	
	@RequestMapping(value = "/deleteRFC/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteRFC(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			RFC rfc = rfcService.findById(id);
			if (rfc.getStatus().getName().equals("Borrador")) {
				
					StatusRFC status = statusService.findByKey("name", "Anulado");
					rfc.setStatus(status);
					rfc.setMotive(status.getReason());
					rfc.setOperator(getUserLogin().getFullName());
					rfcService.update(rfc);
				
			} else {
				res.setStatus("fail");
				res.setException("La acción no se pudo completar, el release no esta en estado de Borrador.");
			}
		} catch (Exception e) {
			Sentry.capture(e, "release");
			res.setStatus("exception");
			res.setException("La acción no se pudo completar correctamente.");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	public void loadCountsRFC(HttpServletRequest request, Integer id) {
		//PUser userLogin = getUserLogin();
		//List<PSystem> systems = systemService.listProjects(userLogin.getId());
		Map<String, Integer> rfcC = new HashMap<String, Integer>();
		rfcC.put("draft", rfcService.countByType(id, "Borrador", 2, null));
		rfcC.put("requested", rfcService.countByType(id, "Solicitado", 2, null));
		rfcC.put("completed", rfcService.countByType(id, "Completado", 2, null));
		rfcC.put("all", (rfcC.get("draft") + rfcC.get("requested") + rfcC.get("completed")));
		request.setAttribute("rfcC", rfcC);

		
	}
	
}

package com.soin.sgrm.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.AttentionGroup;
import com.soin.sgrm.model.StatusIncidence;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.System_StatusIn;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.AttentionGroupService;
import com.soin.sgrm.service.StatusIncidenceService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.System_StatusInService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/systemStatusIn")
public class System_StatusInController extends BaseController {
	public static final Logger logger = Logger.getLogger(System_StatusInController.class);

	@Autowired
	StatusIncidenceService statusIncidenceService;
	
	
	
	@Autowired
	System_StatusInService systemStatusInService;

	@Autowired
	SystemService systemService;

	@Autowired 
	AttentionGroupService attentionGroupService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		Integer idUser = getUserLogin().getId();
		List<AttentionGroup> attentionGroups= attentionGroupService.findGroupByUserId(idUser);
		List<Long> listAttentionGroupId=new ArrayList<Long>();
		for(AttentionGroup attentionGroup: attentionGroups) {
			listAttentionGroupId.add(attentionGroup.getId());
		}
		List<System> systemList=systemService.findByGroupIncidence(listAttentionGroupId);

		model.addAttribute("systems", systemList);
		return "/systemStatusIn/systemStatusIn";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<System_StatusIn> systemStatus = new JsonSheet<>();
		try {
			Integer idUser = getUserLogin().getId();
			systemStatus.setData(systemStatusInService.findByManger(idUser));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return systemStatus;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody System_StatusIn addSystemStatusIn) {
		JsonResponse res = new JsonResponse();
		try {
			
			System_StatusIn systemStatus= systemStatusInService.findByIdAndSys(addSystemStatusIn.getSystemId(),addSystemStatusIn.getStatusId());
			if(systemStatus==null) {
			res.setStatus("success");
			addSystemStatusIn.setSystem(systemService.findSystemById(addSystemStatusIn.getSystemId()));
			addSystemStatusIn.setStatus(statusIncidenceService.findById(addSystemStatusIn.getStatusId()));
			systemStatusInService.save(addSystemStatusIn);
			res.setMessage("Estado agregado al sistema!");
			}else {
				
				res.setStatus("error");
				res.setMessage("Error al agregar estado ya se encuentra un estado similar!");
			}
		} catch (Exception e) {
			Sentry.capture(e, "systemStatusIn");
			res.setStatus("error");
			res.setMessage("Error al agregar estado al sistema!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request,
			@RequestBody System_StatusIn uptSystemStatusIn) {
		JsonResponse res = new JsonResponse();
		try {
			
			System_StatusIn systemPriority= systemStatusInService.findByIdAndSys(uptSystemStatusIn.getSystemId(),uptSystemStatusIn.getStatusId());
			if(systemPriority==null) {
			res.setStatus("success");
			uptSystemStatusIn.setSystem(systemService.findSystemById(uptSystemStatusIn.getSystemId()));
			uptSystemStatusIn.setStatus(statusIncidenceService.findById(uptSystemStatusIn.getStatusId()));
		
			systemStatusInService.update(uptSystemStatusIn);
			res.setMessage("Estado ticket modificado!");
			}else if(systemPriority.getId()==uptSystemStatusIn.getId()) {
				res.setStatus("success");
				uptSystemStatusIn.setSystem(systemService.findSystemById(uptSystemStatusIn.getSystemId()));
				uptSystemStatusIn.setStatus(statusIncidenceService.findById(uptSystemStatusIn.getStatusId()));
	
				systemStatusInService.update(uptSystemStatusIn);
				res.setMessage("Estado ticket modificado!");
			}else {
				res.setStatus("error");
				res.setMessage("Error al actualizar estado ya se encuentra un estado similar!");
			}

		} catch (Exception e) {
			Sentry.capture(e, "systemStatusIn");
			res.setStatus("error");
			res.setMessage("Error al modificar estado!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			systemStatusInService.delete(id);
			res.setStatus("success");
			res.setMessage("Se elimino el estado correctamente!");
		} catch (Exception e) {
			Sentry.capture(e, "systemStatusIn");
			res.setStatus("error");
			res.setMessage("Error al eliminar el estado!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = { "/changeStatus/{id}" }, method = RequestMethod.GET)
	public @ResponseBody List<StatusIncidence> changeStatus(@PathVariable Integer id, Locale locale, Model model) {
		List<StatusIncidence> listStatus = new ArrayList<StatusIncidence>();
		List<StatusIncidence> listAllStatus = statusIncidenceService.findAll();
		List<System_StatusIn> listSystemStatus = systemStatusInService.findBySystem(id);

		try {
			for (StatusIncidence status : listAllStatus) {
				Boolean veri = false;
				if(listSystemStatus.size()>0) {
				for (System_StatusIn systemPriority : listSystemStatus) {
					
					if (systemPriority.getStatus().getId() == status.getId()) {
						veri = true;
					}
					
				}
				}else {
					veri=false;
				}
				if (!veri) {
					listStatus.add(status);
				}
			}
		} catch (Exception e) {
			Sentry.capture(e, "systemStatus");

			e.printStackTrace();
		}

		return listStatus;
	}
	
	
	@RequestMapping(value = { "/getStatus/{id}" }, method = RequestMethod.GET)
	public @ResponseBody List<System_StatusIn> getPriority(@PathVariable Integer id, Locale locale, Model model) {
		List<System_StatusIn> listSystemStatus=null;
		try {
			listSystemStatus = systemStatusInService.findBySystem(id);
			
		} catch (Exception e) {
			Sentry.capture(e, "systemStatus");

			e.printStackTrace();
		}

		return listSystemStatus;
	}
	
	@RequestMapping(value = "/changeSla/{id}", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse changeSla(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			System_StatusIn statusUpt=systemStatusInService.findById(id);
			if(statusUpt.getSlaActive()==1) {
				statusUpt.setSlaActive(0);
				res.setMessage("SLA desactivado!");
			}else {
				statusUpt.setSlaActive(1);
				res.setMessage("SLA activada!");
			}
			res.setStatus("success");
			systemStatusInService.update(statusUpt);
			
		} catch (Exception e) {
			Sentry.capture(e, "typeIncidence");
			res.setStatus("error");
			res.setMessage("Error al modificar el Tipo incidencia!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}

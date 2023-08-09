package com.soin.sgrm.controller;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import com.soin.sgrm.model.pos.PAttentionGroup;
import com.soin.sgrm.model.pos.PStatusIncidence;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PSystem_StatusIn;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.AttentionGroupService;
import com.soin.sgrm.service.StatusIncidenceService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.System_StatusInService;
import com.soin.sgrm.service.pos.PAttentionGroupService;
import com.soin.sgrm.service.pos.PStatusIncidenceService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.service.pos.PSystem_StatusInService;
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
	
	@Autowired
	PStatusIncidenceService pstatusIncidenceService;

	@Autowired
	PSystem_StatusInService psystemStatusInService;

	@Autowired
	PSystemService psystemService;

	@Autowired 
	PAttentionGroupService pattentionGroupService;

	private final Environment environment;

	@Autowired
	public System_StatusInController(Environment environment) {
		this.environment = environment;
	}

	public String profileActive() {
		String[] activeProfiles = environment.getActiveProfiles();
		for (String profile : activeProfiles) {
			return profile;
		}
		return "";
	}
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		Integer idUser = getUserLogin().getId();
		if (profileActive().equals("oracle")) {
			List<AttentionGroup> attentionGroups= attentionGroupService.findGroupByUserId(idUser);
			List<Long> listAttentionGroupId=new ArrayList<Long>();
			for(AttentionGroup attentionGroup: attentionGroups) {
				listAttentionGroupId.add(attentionGroup.getId());
			}
			List<System> systemList=systemService.findByGroupIncidence(listAttentionGroupId);
			Set<System> systemWithRepeat = new LinkedHashSet<>(systemList);
			systemList.clear();
			systemList.addAll(systemWithRepeat);
			model.addAttribute("systems", systemList);
		} else if (profileActive().equals("postgres")) {
			List<PAttentionGroup> attentionGroups= pattentionGroupService.findGroupByUserId(idUser);
			List<Long> listAttentionGroupId=new ArrayList<Long>();
			for(PAttentionGroup attentionGroup: attentionGroups) {
				listAttentionGroupId.add(attentionGroup.getId());
			}
			List<PSystem> systemList=psystemService.findByGroupIncidence(listAttentionGroupId);
			Set<PSystem> systemWithRepeat = new LinkedHashSet<>(systemList);
			systemList.clear();
			systemList.addAll(systemWithRepeat);
			model.addAttribute("systems", systemList);
		}
		
		
		return "/systemStatusIn/systemStatusIn";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		
		try {
			Integer idUser = getUserLogin().getId();
			if (profileActive().equals("oracle")) {
				JsonSheet<System_StatusIn> systemStatus = new JsonSheet<>();
				systemStatus.setData(systemStatusInService.findByManger(idUser));
				return systemStatus;
			} else if (profileActive().equals("postgres")) {
				JsonSheet<PSystem_StatusIn> systemStatus = new JsonSheet<>();
				systemStatus.setData(psystemStatusInService.findByManger(idUser));
				return systemStatus;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody System_StatusIn addSystemStatusIn) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
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
			} else if (profileActive().equals("postgres")) {
				PSystem_StatusIn systemStatus= psystemStatusInService.findByIdAndSys(addSystemStatusIn.getSystemId(),addSystemStatusIn.getStatusId());
				PSystem_StatusIn paddSystemStatusIn =new PSystem_StatusIn();
				if(systemStatus==null) {
				res.setStatus("success");
				paddSystemStatusIn.setSlaActive(addSystemStatusIn.getSlaActive());
				paddSystemStatusIn.setSystem(psystemService.findSystemById(addSystemStatusIn.getSystemId()));
				paddSystemStatusIn.setStatus(pstatusIncidenceService.findById(addSystemStatusIn.getStatusId()));
				psystemStatusInService.save(paddSystemStatusIn);
				res.setMessage("Estado agregado al sistema!");
				}else {
					
					res.setStatus("error");
					res.setMessage("Error al agregar estado ya se encuentra un estado similar!");
				}
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
			if (profileActive().equals("oracle")) {
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
			} else if (profileActive().equals("postgres")) {
				PSystem_StatusIn systemPriority= psystemStatusInService.findByIdAndSys(uptSystemStatusIn.getSystemId(),uptSystemStatusIn.getStatusId());
				PSystem_StatusIn puptSystemStatusIn=new PSystem_StatusIn();
				puptSystemStatusIn.setId(uptSystemStatusIn.getId());
				puptSystemStatusIn.setSlaActive(uptSystemStatusIn.getSlaActive());
				if(systemPriority==null) {
				res.setStatus("success");
				puptSystemStatusIn.setSystem(psystemService.findSystemById(uptSystemStatusIn.getSystemId()));
				puptSystemStatusIn.setStatus(pstatusIncidenceService.findById(uptSystemStatusIn.getStatusId()));
			
				psystemStatusInService.update(puptSystemStatusIn);
				res.setMessage("Estado ticket modificado!");
				}else if(systemPriority.getId()==uptSystemStatusIn.getId()) {
					res.setStatus("success");
					puptSystemStatusIn.setSystem(psystemService.findSystemById(uptSystemStatusIn.getSystemId()));
					puptSystemStatusIn.setStatus(pstatusIncidenceService.findById(uptSystemStatusIn.getStatusId()));
		
					psystemStatusInService.update(puptSystemStatusIn);
					res.setMessage("Estado ticket modificado!");
				}else {
					res.setStatus("error");
					res.setMessage("Error al actualizar estado ya se encuentra un estado similar!");
				}
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
			if (profileActive().equals("oracle")) {
				systemStatusInService.delete(id);
			} else if (profileActive().equals("postgres")) {
				psystemStatusInService.delete(id);
			}
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
	public @ResponseBody List<?> changeStatus(@PathVariable Integer id, Locale locale, Model model) {
		

		try {
			if (profileActive().equals("oracle")) {
				List<StatusIncidence> listStatus = new ArrayList<StatusIncidence>();
				List<StatusIncidence> listAllStatus = statusIncidenceService.findAll();
				List<System_StatusIn> listSystemStatus = systemStatusInService.findBySystem(id);
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
				return listStatus;
			} else if (profileActive().equals("postgres")) {
				List<PStatusIncidence> listStatus = new ArrayList<PStatusIncidence>();
				List<PStatusIncidence> listAllStatus = pstatusIncidenceService.findAll();
				List<PSystem_StatusIn> listSystemStatus = psystemStatusInService.findBySystem(id);
				for (PStatusIncidence status : listAllStatus) {
					Boolean veri = false;
					if(listSystemStatus.size()>0) {
					for (PSystem_StatusIn systemPriority : listSystemStatus) {
						
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
				return listStatus;
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "systemStatus");

			e.printStackTrace();
		}

		return null;
	}
	
	
	@RequestMapping(value = { "/getStatus/{id}" }, method = RequestMethod.GET)
	public @ResponseBody List<?> getPriority(@PathVariable Integer id, Locale locale, Model model) {
		
		try {
			if (profileActive().equals("oracle")) {
				List<System_StatusIn> listSystemStatus=null;
				listSystemStatus = systemStatusInService.findBySystem(id);
				return listSystemStatus;
			} else if (profileActive().equals("postgres")) {
				List<PSystem_StatusIn> listSystemStatus=null;
				listSystemStatus = psystemStatusInService.findBySystem(id);
				return listSystemStatus;
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "systemStatus");

			e.printStackTrace();
		}

		return null;
	}
	
	@RequestMapping(value = "/changeSla/{id}", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse changeSla(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
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
			} else if (profileActive().equals("postgres")) {
				PSystem_StatusIn statusUpt=psystemStatusInService.findById(id);
				if(statusUpt.getSlaActive()==1) {
					statusUpt.setSlaActive(0);
					res.setMessage("SLA desactivado!");
				}else {
					statusUpt.setSlaActive(1);
					res.setMessage("SLA activada!");
				}
				res.setStatus("success");
				psystemStatusInService.update(statusUpt);
			}
			
			
		} catch (Exception e) {
			Sentry.capture(e, "typeIncidence");
			res.setStatus("error");
			res.setMessage("Error al modificar el Tipo incidencia!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}

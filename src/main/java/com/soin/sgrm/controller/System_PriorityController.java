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
import com.soin.sgrm.model.PriorityIncidence;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.System_Priority;
import com.soin.sgrm.model.pos.PAttentionGroup;
import com.soin.sgrm.model.pos.PPriorityIncidence;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PSystem_Priority;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.AttentionGroupService;
import com.soin.sgrm.service.PriorityIncidenceService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.System_PriorityService;
import com.soin.sgrm.service.pos.PAttentionGroupService;
import com.soin.sgrm.service.pos.PPriorityIncidenceService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.service.pos.PSystem_PriorityService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/systemPriority")
public class System_PriorityController extends BaseController {
	public static final Logger logger = Logger.getLogger(System_PriorityController.class);

	@Autowired
	System_PriorityService systemPriorityService;

	@Autowired
	SystemService systemService;

	@Autowired
	PriorityIncidenceService priorityService;
	
	@Autowired
	AttentionGroupService attentionGroupService;
	
	@Autowired
	PSystem_PriorityService psystemPriorityService;

	@Autowired
	PSystemService psystemService;

	@Autowired
	PPriorityIncidenceService ppriorityService;
	
	@Autowired
	PAttentionGroupService pattentionGroupService;
	
	private final Environment environment;

	@Autowired
	public System_PriorityController(Environment environment) {
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

		return "/systemPriority/systemPriority";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		
		try {
			Integer idUser = getUserLogin().getId();
			if (profileActive().equals("oracle")) {
				JsonSheet<System_Priority> systemPriority = new JsonSheet<>();
				systemPriority.setData(systemPriorityService.findByManger(idUser));
				return systemPriority;
			} else if (profileActive().equals("postgres")) {
				JsonSheet<PSystem_Priority> systemPriority = new JsonSheet<>();
				systemPriority.setData(psystemPriorityService.findByManger(idUser));
				return systemPriority;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody System_Priority addSystemPriority) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				System_Priority systemPriority= systemPriorityService.findByIdAndSys(addSystemPriority.getSystemId(),addSystemPriority.getPriorityId());
				if(systemPriority==null) {
				res.setStatus("success");
				addSystemPriority.setSystem(systemService.findSystemById(addSystemPriority.getSystemId()));
				addSystemPriority.setPriority(priorityService.findById(addSystemPriority.getPriorityId()));
				if(addSystemPriority.getSla()==0) {
					addSystemPriority.setTime(null);
				}
				systemPriorityService.save(addSystemPriority);
				res.setMessage("Prioridad agregada al sistema!");
				}else {
					
					res.setStatus("error");
					res.setMessage("Error al agregar prioridad ya se encuentra una prioridad similar!");
				}
			} else if (profileActive().equals("postgres")) {
				PSystem_Priority systemPriority= psystemPriorityService.findByIdAndSys(addSystemPriority.getSystemId(),addSystemPriority.getPriorityId());
				PSystem_Priority paddSystemPriority=new PSystem_Priority();
				paddSystemPriority.setTime(addSystemPriority.getTime());
				paddSystemPriority.setSla(addSystemPriority.getSla());
				if(systemPriority==null) {
				res.setStatus("success");
				paddSystemPriority.setSystem(psystemService.findSystemById(addSystemPriority.getSystemId()));
				paddSystemPriority.setPriority(ppriorityService.findById(addSystemPriority.getPriorityId()));
				if(paddSystemPriority.getSla()==0) {
					paddSystemPriority.setTime(null);
				}
				psystemPriorityService.save(paddSystemPriority);
				res.setMessage("Prioridad agregada al sistema!");
				}else {
					
					res.setStatus("error");
					res.setMessage("Error al agregar prioridad ya se encuentra una prioridad similar!");
				}
			}
			
			
		} catch (Exception e) {
			Sentry.capture(e, "systemPriority");
			res.setStatus("error");
			res.setMessage("Error al agregar prioridad al sistema!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request,
			@RequestBody System_Priority uptSystemPriority) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				System_Priority systemPriority= systemPriorityService.findByIdAndSys(uptSystemPriority.getSystemId(),uptSystemPriority.getPriorityId());
				if(systemPriority==null) {
				res.setStatus("success");
				uptSystemPriority.setSystem(systemService.findSystemById(uptSystemPriority.getSystemId()));
				uptSystemPriority.setPriority(priorityService.findById(uptSystemPriority.getPriorityId()));
				if(uptSystemPriority.getSla()==0) {
					uptSystemPriority.setTime(null);
				}
				systemPriorityService.update(uptSystemPriority);
				res.setMessage("Tipo solicitud modificada!");
				}else if(systemPriority.getId()==uptSystemPriority.getId()) {
					res.setStatus("success");
					uptSystemPriority.setSystem(systemService.findSystemById(uptSystemPriority.getSystemId()));
					uptSystemPriority.setPriority(priorityService.findById(uptSystemPriority.getPriorityId()));
					if(uptSystemPriority.getSla()==0) {
						uptSystemPriority.setTime(null);
					}
					systemPriorityService.update(uptSystemPriority);
					res.setMessage("Tipo solicitud modificada!");
				}else {
					res.setStatus("error");
					res.setMessage("Error al actualizar prioridad ya se encuentra una prioridad similar!");
				}

			} else if (profileActive().equals("postgres")) {
				PSystem_Priority systemPriority= psystemPriorityService.findByIdAndSys(uptSystemPriority.getSystemId(),uptSystemPriority.getPriorityId());
				PSystem_Priority puptSystemPriority=new PSystem_Priority();
				puptSystemPriority.setId(uptSystemPriority.getId());
				puptSystemPriority.setTime(uptSystemPriority.getTime());
				puptSystemPriority.setSla(uptSystemPriority.getSla());
				if(systemPriority==null) {
				res.setStatus("success");
				
				puptSystemPriority.setSystem(psystemService.findSystemById(uptSystemPriority.getSystemId()));
				puptSystemPriority.setPriority(ppriorityService.findById(uptSystemPriority.getPriorityId()));
				if(puptSystemPriority.getSla()==0) {
					puptSystemPriority.setTime(null);
				}
				psystemPriorityService.update(puptSystemPriority);
				res.setMessage("Tipo solicitud modificada!");
				}else if(systemPriority.getId()==uptSystemPriority.getId()) {
					res.setStatus("success");
					puptSystemPriority.setSystem(psystemService.findSystemById(uptSystemPriority.getSystemId()));
					puptSystemPriority.setPriority(ppriorityService.findById(uptSystemPriority.getPriorityId()));
					if(uptSystemPriority.getSla()==0) {
						uptSystemPriority.setTime(null);
					}
					psystemPriorityService.update(puptSystemPriority);
					res.setMessage("Tipo solicitud modificada!");
				}else {
					res.setStatus("error");
					res.setMessage("Error al actualizar prioridad ya se encuentra una prioridad similar!");
				}

			}
			
		} catch (Exception e) {
			Sentry.capture(e, "systemPriority");
			res.setStatus("error");
			res.setMessage("Error al modificar prioridad!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				systemPriorityService.delete(id);
			} else if (profileActive().equals("postgres")) {
				psystemPriorityService.delete(id);
			}
			
			res.setStatus("success");
			res.setMessage("Se elimino la prioridad correctamente!");
		} catch (Exception e) {
			Sentry.capture(e, "systemPriority");
			res.setStatus("error");
			res.setMessage("Error al eliminar la prioridad!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = { "/changePriority/{id}" }, method = RequestMethod.GET)
	public @ResponseBody List<?> changePriority(@PathVariable Integer id, Locale locale, Model model) {
		

		try {
			if (profileActive().equals("oracle")) {
				List<PriorityIncidence> listPriority = new ArrayList<PriorityIncidence>();
				List<PriorityIncidence> listAllPriority = priorityService.findAll();
				List<System_Priority> listSystemPriority = systemPriorityService.findBySystem(id);
				for (PriorityIncidence priority : listAllPriority) {
					Boolean veri = false;
					if(listSystemPriority.size()>0) {
					for (System_Priority systemPriority : listSystemPriority) {
						
						if (systemPriority.getPriority().getId() == priority.getId()) {
							veri = true;
						}
						
					}
					}else {
						veri=false;
					}
					if (!veri) {
						listPriority.add(priority);
					}
				}
				return listPriority;
			} else if (profileActive().equals("postgres")) {
				List<PPriorityIncidence> listPriority = new ArrayList<PPriorityIncidence>();
				List<PPriorityIncidence> listAllPriority = ppriorityService.findAll();
				List<PSystem_Priority> listSystemPriority = psystemPriorityService.findBySystem(id);
				for (PPriorityIncidence priority : listAllPriority) {
					Boolean veri = false;
					if(listSystemPriority.size()>0) {
					for (PSystem_Priority systemPriority : listSystemPriority) {
						
						if (systemPriority.getPriority().getId() == priority.getId()) {
							veri = true;
						}
						
					}
					}else {
						veri=false;
					}
					if (!veri) {
						listPriority.add(priority);
					}
				}
				return listPriority;
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "systemPriority");

			e.printStackTrace();
		}

		return null;
	}
	
	@RequestMapping(value = { "/getPriority/{id}" }, method = RequestMethod.GET)
	public @ResponseBody List<?> getPriority(@PathVariable Integer id, Locale locale, Model model) {
		
		try {
			if (profileActive().equals("oracle")) {
				List<System_Priority> listSystemPriority=null;
				listSystemPriority = systemPriorityService.findBySystem(id);
				return listSystemPriority;
			} else if (profileActive().equals("postgres")) {
				List<PSystem_Priority> listSystemPriority=null;
				listSystemPriority = psystemPriorityService.findBySystem(id);
				return listSystemPriority;
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "systemPriority");

			e.printStackTrace();
		}

		return null;
	}
}
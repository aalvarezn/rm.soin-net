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
import com.soin.sgrm.model.PriorityIncidence;
import com.soin.sgrm.model.System_Priority;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.PriorityIncidenceService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.System_PriorityService;
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

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		Integer idUser = getUserLogin().getId();
		List<com.soin.sgrm.model.System> systems = systemService.findByManagerIncidence(idUser);

		model.addAttribute("systems", systems);
		return "/systemPriority/systemPriority";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<System_Priority> systemPriority = new JsonSheet<>();
		try {
			systemPriority.setData(systemPriorityService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return systemPriority;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody System_Priority addSystemPriority) {
		JsonResponse res = new JsonResponse();
		try {
			
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
			systemPriorityService.delete(id);
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
	public @ResponseBody List<PriorityIncidence> changePriority(@PathVariable Integer id, Locale locale, Model model) {
		List<PriorityIncidence> listPriority = new ArrayList<PriorityIncidence>();
		List<PriorityIncidence> listAllPriority = priorityService.findAll();
		List<System_Priority> listSystemPriority = systemPriorityService.findBySystem(id);

		try {
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
		} catch (Exception e) {
			Sentry.capture(e, "systemPriority");

			e.printStackTrace();
		}

		return listPriority;
	}
	
	@RequestMapping(value = { "/getPriority/{id}" }, method = RequestMethod.GET)
	public @ResponseBody List<System_Priority> getPriority(@PathVariable Integer id, Locale locale, Model model) {
		List<System_Priority> listSystemPriority=null;
		try {
			listSystemPriority = systemPriorityService.findBySystem(id);
			
		} catch (Exception e) {
			Sentry.capture(e, "systemPriority");

			e.printStackTrace();
		}

		return listSystemPriority;
	}
}

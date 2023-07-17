package com.soin.sgrm.controller.admin;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.Risk;
import com.soin.sgrm.model.pos.PPriority;
import com.soin.sgrm.model.pos.PRisk;
import com.soin.sgrm.service.PriorityService;
import com.soin.sgrm.service.pos.PPriorityService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/priority")
public class PriorityController extends BaseController {

	public static final Logger logger = Logger.getLogger(PriorityController.class);

	@Autowired
	PriorityService priorityService;

	@Autowired
	PPriorityService ppriorityService;
	
	private final Environment environment;

	@Autowired
	public PriorityController(Environment environment) {
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
		
		String profile = profileActive();
		if (profile.equals("oracle")) {
			model.addAttribute("prioritys", priorityService.list());
			model.addAttribute("priority", new Priority());
		} else if (profile.equals("postgres")) {
			model.addAttribute("prioritys", ppriorityService.list());
			model.addAttribute("priority", new PPriority());
		}
		
		return "/admin/priority/priority";
	}

	@RequestMapping(value = "/findPriority/{id}", method = RequestMethod.GET)
	public @ResponseBody Object findPriority(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		try {
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				Priority priority = priorityService.findById(id);
				return priority;
			} else if (profile.equals("postgres")) {
				PPriority priority = ppriorityService.findById(id);
				return priority;
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "priority");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
		return null;
	}

	@RequestMapping(path = "/savePriority", method = RequestMethod.POST)
	public @ResponseBody JsonResponse savePriority(HttpServletRequest request,

			@Valid @ModelAttribute("Priority") Priority priority, BindingResult errors, ModelMap model, Locale locale,
			HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");

			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}
			if (res.getStatus().equals("success")) {
				String profile = profileActive();
				if (profile.equals("oracle")) {
					priorityService.save(priority);
					res.setObj(priority);
				} else if (profile.equals("postgres")) {
					PPriority ppriority=new PPriority();
					ppriority.setDescription(priority.getDescription());
					ppriority.setName(priority.getName());
					ppriorityService.save(ppriority);
					res.setObj(priority);
				}
				
			}
		} catch (Exception e) {
			Sentry.capture(e, "priority");
			res.setStatus("exception");
			res.setException("Error al crear prioridad: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/updatePriority", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updatePriority(HttpServletRequest request,
			@Valid @ModelAttribute("Priority") Priority priority, BindingResult errors, ModelMap model, Locale locale,
			HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}
			if (res.getStatus().equals("success")) {
				String profile = profileActive();
				if (profile.equals("oracle")) {
					Priority priorityOrigin = priorityService.findById(priority.getId());
					priorityOrigin.setName(priority.getName());
					priorityOrigin.setDescription(priority.getDescription());
					priorityService.update(priorityOrigin);
					res.setObj(priority);
				} else if (profile.equals("postgres")) {
					PPriority ppriorityOrigin = ppriorityService.findById(priority.getId());
					ppriorityOrigin.setName(priority.getName());
					ppriorityOrigin.setDescription(priority.getDescription());
					ppriorityOrigin.setId(priority.getId());
					ppriorityService.update(ppriorityOrigin);
					res.setObj(ppriorityOrigin);
				}
				

			}
		} catch (Exception e) {
			Sentry.capture(e, "priority");
			res.setStatus("exception");
			res.setException("Error al modificar prioridad: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deletePriority/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deletePriority(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				priorityService.delete(id);
			} else if (profile.equals("postgres")) {
				ppriorityService.delete(id);
			}
		
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar prioridad: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar prioridad: Existen referencias que debe eliminar antes");
			} else {
				Sentry.capture(e, "priority");
			}
		}
		return res;
	}

}

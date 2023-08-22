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
import com.soin.sgrm.model.ActionEnvironment;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.pos.PActionEnvironment;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.service.ActionEnvironmentService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.pos.PActionEnvironmentService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/action")
public class ActionEnvironmentController extends BaseController {

	public static final Logger logger = Logger.getLogger(ActionEnvironmentController.class);

	@Autowired
	ActionEnvironmentService actionEnvironmentService;

	@Autowired
	SystemService systemService;
	
	@Autowired
	PActionEnvironmentService pactionEnvironmentService;

	@Autowired
	PSystemService psystemService;

	private final Environment environment;
	
	
	@Autowired
	public ActionEnvironmentController(Environment environment) {
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
			model.addAttribute("actionEnvironments", actionEnvironmentService.list());
			model.addAttribute("actionEnvironment", new ActionEnvironment());
			model.addAttribute("systems", systemService.listAll());
			model.addAttribute("system", new SystemInfo());
		} else if (profile.equals("postgres")) {
			model.addAttribute("actionEnvironments", pactionEnvironmentService.list());
			model.addAttribute("actionEnvironment", new PActionEnvironment());
			model.addAttribute("systems", psystemService.listAll());
			model.addAttribute("system", new PSystemInfo());
		}
		
		return "/admin/actionEnvironment/actionEnvironment";
	}

	@RequestMapping(value = "/findActionEnvironment/{id}", method = RequestMethod.GET)
	public @ResponseBody Object findActionEnvironment(@PathVariable Integer id, HttpServletRequest request,
			Locale locale, Model model, HttpSession session) {
		try {
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				ActionEnvironment actionEnvironment = actionEnvironmentService.findActionById(id);
				return actionEnvironment;
			} else if (profile.equals("postgres")) {
				PActionEnvironment pactionEnvironment = pactionEnvironmentService.findActionById(id);
				return pactionEnvironment;
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "action");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
		return null;
	}

	@RequestMapping(path = "/saveActionEnvironment", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveActionEnvironment(HttpServletRequest request,

			@Valid @ModelAttribute("ActionEnvironment") ActionEnvironment actionEnvironment, BindingResult errors,
			ModelMap model, Locale locale, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");

			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}

			if (actionEnvironment.getSystemId() == null) {
				res.setStatus("fail");
				res.addError("systemId", "Seleccione una opción");
			}

			if (res.getStatus().equals("success")) {
				
				String profile = profileActive();
				if (profile.equals("oracle")) {
					actionEnvironment.setSystem(systemService.findById(actionEnvironment.getSystemId()));
					actionEnvironmentService.save(actionEnvironment);
					res.setObj(actionEnvironment);
				} else if (profile.equals("postgres")) {
					
					PActionEnvironment pactionEnvironment=new PActionEnvironment();
					pactionEnvironment.setSystem(psystemService.findById(actionEnvironment.getSystemId()));
					pactionEnvironment.setName(actionEnvironment.getName());
					pactionEnvironmentService.save(pactionEnvironment);
					res.setObj(actionEnvironment);
				}
				
			}
		} catch (Exception e) {
			Sentry.capture(e, "action");
			res.setStatus("exception");
			res.setException("Error al crear acción: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/updateActionEnvironment", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateActionEnvironment(HttpServletRequest request,
			@Valid @ModelAttribute("ActionEnvironment") ActionEnvironment actionEnvironment, BindingResult errors,
			ModelMap model, Locale locale, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}
			if (actionEnvironment.getSystemId() == null) {
				res.setStatus("fail");
				res.addError("systemId", "Seleccione una opción");
			}

			if (res.getStatus().equals("success")) {
				
				String profile = profileActive();
				if (profile.equals("oracle")) {
					ActionEnvironment actionEnvironmentOrigin = actionEnvironmentService
							.findActionById(actionEnvironment.getId());
					actionEnvironmentOrigin.setName(actionEnvironment.getName());
					actionEnvironmentOrigin.setSystem(systemService.findById(actionEnvironment.getSystemId()));
					actionEnvironmentService.update(actionEnvironmentOrigin);
					res.setObj(actionEnvironment);
				} else if (profile.equals("postgres")) {
					PActionEnvironment pactionEnvironmentOrigin = pactionEnvironmentService
							.findActionById(actionEnvironment.getId());
					pactionEnvironmentOrigin.setName(actionEnvironment.getName());
					pactionEnvironmentOrigin.setSystem(psystemService.findById(actionEnvironment.getSystemId()));
					pactionEnvironmentService.update(pactionEnvironmentOrigin);
					res.setObj(pactionEnvironmentOrigin);
				}
				
			}
		} catch (Exception e) {
			Sentry.capture(e, "action");
			res.setStatus("exception");
			res.setException("Error al modificar acción: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteActionEnvironment/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteActionEnvironment(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			String profile = profileActive();
			if (profile.equals("oracle")) {
				actionEnvironmentService.delete(id);
			} else if (profile.equals("postgres")) {
				pactionEnvironmentService.delete(id);
			}
			
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar acción: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar acción: Existen referencias que debe eliminar antes");
			} else {
				Sentry.capture(e, "action");
			}
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}

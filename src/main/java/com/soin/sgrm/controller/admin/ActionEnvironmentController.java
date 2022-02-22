package com.soin.sgrm.controller.admin;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.soin.sgrm.service.ActionEnvironmentService;
import com.soin.sgrm.service.SystemService;
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

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("actionEnvironments", actionEnvironmentService.list());
		model.addAttribute("actionEnvironment", new ActionEnvironment());
		model.addAttribute("systems", systemService.listAll());
		model.addAttribute("system", new SystemInfo());
		return "/admin/actionEnvironment/actionEnvironment";
	}

	@RequestMapping(value = "/findActionEnvironment/{id}", method = RequestMethod.GET)
	public @ResponseBody ActionEnvironment findActionEnvironment(@PathVariable Integer id, HttpServletRequest request,
			Locale locale, Model model, HttpSession session) {
		try {
			ActionEnvironment actionEnvironment = actionEnvironmentService.findActionById(id);
			return actionEnvironment;
		} catch (Exception e) {
			Sentry.capture(e, "action");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
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
				actionEnvironment.setSystem(systemService.findById(actionEnvironment.getSystemId()));
				actionEnvironmentService.save(actionEnvironment);
				res.setObj(actionEnvironment);
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
				ActionEnvironment actionEnvironmentOrigin = actionEnvironmentService
						.findActionById(actionEnvironment.getId());
				actionEnvironmentOrigin.setName(actionEnvironment.getName());
				actionEnvironmentOrigin.setSystem(systemService.findById(actionEnvironment.getSystemId()));
				actionEnvironmentService.update(actionEnvironmentOrigin);
				res.setObj(actionEnvironment);
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
			actionEnvironmentService.delete(id);
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

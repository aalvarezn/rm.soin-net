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
import com.soin.sgrm.model.Environment;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.service.EnvironmentService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/environment")
public class EnvironmentController extends BaseController {
	
	public static final Logger logger = Logger.getLogger(EnvironmentController.class);

	@Autowired
	EnvironmentService environmentService;

	@Autowired
	SystemService systemService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("environments", environmentService.list());
		model.addAttribute("environment", new Environment());
		model.addAttribute("systems", systemService.listAll());
		model.addAttribute("system", new SystemInfo());
		return "/admin/environment/environment";
	}

	@RequestMapping(value = "/findEnvironment/{id}", method = RequestMethod.GET)
	public @ResponseBody Environment findEnvironment(@PathVariable Integer id, HttpServletRequest request,
			Locale locale, Model model, HttpSession session) {
		try {
			Environment environment = environmentService.findById(id);
			return environment;
		} catch (Exception e) {
			Sentry.capture(e,"environment");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}

	@RequestMapping(path = "/saveEnvironment", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveEnvironment(HttpServletRequest request,
			@Valid @ModelAttribute("Environment") Environment environment, BindingResult errors, ModelMap model,
			Locale locale, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");

			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}

			if (environment.getSystemId() == null) {
				res.setStatus("fail");
				res.addError("systemId", "Seleccione una opción");
			}

			if (res.getStatus().equals("success")) {
				environment.setSystem(systemService.findById(environment.getSystemId()));
				environmentService.save(environment);
				res.setObj(environment);
			}
		} catch (Exception e) {
			Sentry.capture(e,"environment");
			res.setStatus("exception");
			res.setException("Error al crear entorno: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/updateEnvironment", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateEnvironment(HttpServletRequest request,
			@Valid @ModelAttribute("Environment") Environment environment, BindingResult errors, ModelMap model,
			Locale locale, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}
			if (environment.getSystemId() == null) {
				res.setStatus("fail");
				res.addError("systemId", "Seleccione una opción");
			}

			if (res.getStatus().equals("success")) {
				Environment environmentOrigin = environmentService.findById(environment.getId());
				environmentOrigin.setName(environment.getName());
				environmentOrigin.setDescription(environment.getDescription());
				environmentOrigin.setSystem(systemService.findById(environment.getSystemId()));
				environmentOrigin.setExternal(environment.getExternal());
				environmentService.update(environmentOrigin);
				res.setObj(environment);
			}
		} catch (Exception e) {
			Sentry.capture(e,"environment");
			res.setStatus("exception");
			res.setException("Error al modificar entorno: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteEnvironment/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteEnvironment(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			environmentService.delete(id);
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar entorno: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar entorno: Existen referencias que debe eliminar antes");
			}else {
				Sentry.capture(e,"environment");
			}
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}

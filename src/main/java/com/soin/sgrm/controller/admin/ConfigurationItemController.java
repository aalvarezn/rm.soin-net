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
import com.soin.sgrm.model.ConfigurationItem;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.service.ConfigurationItemService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/configurationItem")
public class ConfigurationItemController extends BaseController {

	public static final Logger logger = Logger.getLogger(ConfigurationItemController.class);

	@Autowired
	ConfigurationItemService configurationItemService;

	@Autowired
	SystemService systemService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("configurationItems", configurationItemService.list());
		model.addAttribute("configurationItem", new ConfigurationItem());
		model.addAttribute("systems", systemService.listAll());
		model.addAttribute("system", new SystemInfo());
		return "/admin/configurationItem/configurationItem";
	}

	@RequestMapping(value = "/findConfigurationItem/{id}", method = RequestMethod.GET)
	public @ResponseBody ConfigurationItem findConfigurationItem(@PathVariable Integer id, HttpServletRequest request,
			Locale locale, Model model, HttpSession session) {
		try {
			ConfigurationItem configurationItem = configurationItemService.findById(id);
			return configurationItem;
		} catch (Exception e) {
			Sentry.capture(e, "configuration");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}

	@RequestMapping(path = "/saveConfigurationItem", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveConfigurationItem(HttpServletRequest request,

			@Valid @ModelAttribute("ConfigurationItem") ConfigurationItem configurationItem, BindingResult errors,
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

			if (configurationItem.getSystemId() == null) {
				res.setStatus("fail");
				res.addError("systemId", "Seleccione una opción");
			}

			if (res.getStatus().equals("success")) {
				configurationItem.setSystem(systemService.findById(configurationItem.getSystemId()));
				configurationItemService.save(configurationItem);
				res.setObj(configurationItem);
			}
		} catch (Exception e) {
			Sentry.capture(e, "configuration");
			res.setStatus("exception");
			res.setException("Error al crear item de configuración: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/updateConfigurationItem", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateConfigurationItem(HttpServletRequest request,
			@Valid @ModelAttribute("ConfigurationItem") ConfigurationItem configurationItem, BindingResult errors,
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
			if (configurationItem.getSystemId() == null) {
				res.setStatus("fail");
				res.addError("systemId", "Seleccione una opción");
			}

			if (res.getStatus().equals("success")) {
				ConfigurationItem configurationItemOrigin = configurationItemService
						.findById(configurationItem.getId());
				configurationItemOrigin.setName(configurationItem.getName());
				configurationItemOrigin.setDescription(configurationItem.getDescription());
				configurationItemOrigin.setSystem(systemService.findById(configurationItem.getSystemId()));
				configurationItemService.update(configurationItemOrigin);
				res.setObj(configurationItem);
			}
		} catch (Exception e) {
			Sentry.capture(e, "configuration");
			res.setStatus("exception");
			res.setException("Error al modificar item de configuración: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteConfigurationItem/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteConfigurationItem(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			configurationItemService.delete(id);
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar item de configuración: "
					+ e.getCause().getCause().getCause().getMessage() + ":" + e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException(
						"Error al eliminar item de configuración: Existen referencias que debe eliminar antes");
			} else {
				Sentry.capture(e, "configuration");
			}

			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}

package com.soin.sgrm.controller;

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
import com.soin.sgrm.model.Component;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.ComponentService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/component")
public class ComponentController extends BaseController {
	public static final Logger logger = Logger.getLogger(ComponentController.class);

	@Autowired
	ComponentService componentService;
	
	
	
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {

		return "/incidence/knowledge/component/component";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<Component> Components = new JsonSheet<>();
		try {
			Components.setData(componentService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Components;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody Component addComponent) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");

			componentService.save(addComponent);

			res.setMessage("Componente agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "component");
			res.setStatus("error");
			res.setMessage("Error al agregar componente!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody Component uptComponent) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			componentService.update(uptComponent);

			res.setMessage("Componente modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "Component");
			res.setStatus("exception");
			res.setMessage("Error al modificar componente!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			componentService.delete(id);
			res.setMessage("Componente eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "component");
			res.setStatus("error");
			res.setMessage("Error al eliminar el componente !");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}

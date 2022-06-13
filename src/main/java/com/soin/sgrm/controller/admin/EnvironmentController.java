package com.soin.sgrm.controller.admin;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Sets;
import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.pos.PEnvironment;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.pos.EnvironmentService;
import com.soin.sgrm.service.pos.SystemService;
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
		model.addAttribute("systems", systemService.findAll());
		model.addAttribute("system", new Project());
		return "/admin/environment/environment";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<PEnvironment> environments = new JsonSheet<>();
		try {
			environments.setData(environmentService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return environments;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody PEnvironment addEnvironment) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			List<PSystem> systems=systemService.listSystemByName(addEnvironment.getStrSystems());
			addEnvironment.setSystems(Sets.newHashSet(systems));
		
			environmentService.save(addEnvironment);

			res.setMessage("Entorno agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "environment");
			res.setStatus("exception");
			res.setMessage("Error al agregar Entorno!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody PEnvironment uptEnvironment) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			List<PSystem> systems=systemService.listSystemByName(uptEnvironment.getStrSystems());
			uptEnvironment.setSystems(Sets.newHashSet(systems));
			environmentService.update(uptEnvironment);

			res.setMessage("Entorno modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "environment");
			res.setStatus("exception");
			res.setMessage("Error al modificar Entorno!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
/*
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			environmentService.delete(id);
			res.setMessage("Entorno eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "environment");
			res.setStatus("exception");
			res.setMessage("Error al eliminar el environment!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	*/

}

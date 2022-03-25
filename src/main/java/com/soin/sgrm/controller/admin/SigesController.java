package com.soin.sgrm.controller.admin;

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

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.pos.PSiges;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.pos.SigesService;
import com.soin.sgrm.service.pos.SystemService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/siges")
public class SigesController {
	public static final Logger logger = Logger.getLogger(AmbientController.class);

	@Autowired
	SigesService sigesService;
	
	@Autowired
	SystemService systemService;
	
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("systems", systemService.findAll());
		model.addAttribute("system", new Project());
		return "/admin/siges/siges";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<PSiges> rfcs = new JsonSheet<>();
		try {
			rfcs.setData(sigesService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rfcs;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody PSiges addSiges) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			PSystem system= systemService.findById(addSiges.getSystemId());
			addSiges.setSystem(system);
			sigesService.save(addSiges);

			res.setMessage("Siges agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "siges");
			res.setStatus("exception");
			res.setMessage("Error al agregar siges!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody PSiges uptSiges) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			PSystem system= systemService.findById(uptSiges.getSystemId());
			uptSiges.setSystem(system);
			sigesService.update(uptSiges);

			res.setMessage("Siges modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "siges");
			res.setStatus("exception");
			res.setMessage("Error al modificar siges!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			sigesService.delete(id);
			res.setMessage("Siges eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "siges");
			res.setStatus("exception");
			res.setMessage("Error al eliminar el siges!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}

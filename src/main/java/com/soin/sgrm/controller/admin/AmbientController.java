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
import com.soin.sgrm.model.Ambient;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.TypeAmbient;
import com.soin.sgrm.service.AmbientService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.TypeAmbientService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/ambient")
public class AmbientController extends BaseController {
	
	public static final Logger logger = Logger.getLogger(AmbientController.class);

	@Autowired
	AmbientService ambientService;

	@Autowired
	SystemService systemService;

	@Autowired
	TypeAmbientService typeAmbientService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("ambients", ambientService.list());
		model.addAttribute("ambient", new Ambient());
		model.addAttribute("systems", systemService.listAll());
		model.addAttribute("system", new SystemInfo());
		model.addAttribute("typeAmbients", typeAmbientService.list());
		model.addAttribute("typeAmbient", new TypeAmbient());
		return "/admin/ambient/ambient";
	}

	@RequestMapping(value = "/findAmbient/{id}", method = RequestMethod.GET)
	public @ResponseBody Ambient findAmbient(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		try {
			Ambient ambient = ambientService.findById(id);
			return ambient;
		} catch (Exception e) {
			Sentry.capture(e, "ambient");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}

	@RequestMapping(path = "/saveAmbient", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveAmbient(HttpServletRequest request,

			@Valid @ModelAttribute("Ambient") Ambient ambient, BindingResult errors, ModelMap model, Locale locale,
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
			
			if (ambient.getSystemId() == null) {
				res.setStatus("fail");
				res.addError("systemId", "Seleccione una opción");
			}
			
			if (ambient.getTypeAmbientId() == null) {
				res.setStatus("fail");
				res.addError("typeAmbientId", "Seleccione una opción");
			}
			
			if (res.getStatus().equals("success")) {
				ambient.setSystem(systemService.findSystemUserById(ambient.getSystemId()));
				ambient.setTypeAmbient(typeAmbientService.findById(ambient.getTypeAmbientId()));
				ambientService.save(ambient);
				res.setObj(ambient);
			}
		} catch (Exception e) {
			Sentry.capture(e, "ambient");
			res.setStatus("exception");
			res.setException("Error al crear ambiente: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/updateAmbient", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateAmbient(HttpServletRequest request,
			@Valid @ModelAttribute("Ambient") Ambient ambient, BindingResult errors, ModelMap model, Locale locale,
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
			if (ambient.getSystemId() == null) {
				res.setStatus("fail");
				res.addError("systemId", "Seleccione una opción");
			}
			
			if (ambient.getTypeAmbientId() == null) {
				res.setStatus("fail");
				res.addError("typeAmbientId", "Seleccione una opción");
			}
			
			if (res.getStatus().equals("success")) {
				Ambient ambientOrigin = ambientService.findById(ambient.getId());
				ambientOrigin.setSystem(systemService.findSystemUserById(ambient.getSystemId()));
				ambientOrigin.setTypeAmbient(typeAmbientService.findById(ambient.getTypeAmbientId()));
				ambientOrigin.setCode(ambient.getCode());
				ambientOrigin.setName(ambient.getName());
				ambientOrigin.setDetails(ambient.getDetails());
				ambientOrigin.setServerName(ambient.getServerName());
				ambientService.update(ambientOrigin);
				res.setObj(ambient);
			}
		} catch (Exception e) {
			Sentry.capture(e, "ambient");
			res.setStatus("exception");
			res.setException("Error al modificar ambiente: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteAmbient/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteAmbient(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			ambientService.delete(id);
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar ambiente: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());
			
			if(e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar ambiente: Existen referencias que debe eliminar antes");
			}else {
				Sentry.capture(e, "ambient");
			}
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}

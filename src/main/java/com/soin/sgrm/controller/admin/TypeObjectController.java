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
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.TypeObject;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.service.TypeObjectService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/typeObject")
public class TypeObjectController extends BaseController {

	public static final Logger logger = Logger.getLogger(TypeObjectController.class);

	@Autowired
	TypeObjectService typeObjectService;

	@Autowired
	SystemService systemService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("typeObjects", typeObjectService.list());
		model.addAttribute("typeObject", new TypeObject());
		model.addAttribute("systems", systemService.listAll());
		model.addAttribute("system", new SystemInfo());
		return "/admin/typeObject/typeObject";
	}

	@RequestMapping(value = "/findTypeObject/{id}", method = RequestMethod.GET)
	public @ResponseBody TypeObject findTypeObject(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		try {
			TypeObject typeObject = typeObjectService.findById(id);
			return typeObject;
		} catch (Exception e) {
			Sentry.capture(e, "typeObject");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}

	@RequestMapping(path = "/saveTypeObject", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveTypeObject(HttpServletRequest request,

			@Valid @ModelAttribute("TypeObject") TypeObject typeObject, BindingResult errors, ModelMap model,
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

			if (typeObject.getSystemId() == null) {
				res.setStatus("fail");
				res.addError("systemId", "Seleccione una opción");
			}

			if (res.getStatus().equals("success")) {
				typeObject.setSystem(systemService.findById(typeObject.getSystemId()));
				typeObjectService.save(typeObject);
				res.setObj(typeObject);
			}
		} catch (Exception e) {
			Sentry.capture(e, "typeObject");
			res.setStatus("exception");
			res.setException("Error al crear tipo de objeto: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/updateTypeObject", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateTypeObject(HttpServletRequest request,
			@Valid @ModelAttribute("TypeObject") TypeObject typeObject, BindingResult errors, ModelMap model,
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
			if (typeObject.getSystemId() == null) {
				res.setStatus("fail");
				res.addError("systemId", "Seleccione una opción");
			}

			if (res.getStatus().equals("success")) {
				TypeObject typeObjectOrigin = typeObjectService.findById(typeObject.getId());
				typeObjectOrigin.setName(typeObject.getName());
				typeObjectOrigin.setDescription(typeObject.getDescription());
				typeObjectOrigin.setSystem(systemService.findById(typeObject.getSystemId()));
				typeObjectService.update(typeObjectOrigin);
				res.setObj(typeObject);
			}
		} catch (Exception e) {
			Sentry.capture(e, "typeObject");
			res.setStatus("exception");
			res.setException("Error al modificar tipo de objeto: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			;
		}
		return res;
	}

	@RequestMapping(value = "/deleteTypeObject/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteTypeObject(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			typeObjectService.delete(id);
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar tipo de objeto: " + e.getCause().getCause().getCause().getMessage()
					+ ":" + e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar tipo de objeto: Existen referencias que debe eliminar antes");
			} else {
				Sentry.capture(e, "typeObject");
			}

			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}

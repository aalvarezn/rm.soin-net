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
import com.soin.sgrm.model.TypeAmbient;
import com.soin.sgrm.service.TypeAmbientService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/typeAmbient")
public class TypeAmbientController extends BaseController {

	public static final Logger logger = Logger.getLogger(TypeAmbientController.class);

	@Autowired
	TypeAmbientService typeAmbientService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("typeAmbients", typeAmbientService.list());
		model.addAttribute("typeAmbient", new TypeAmbient());
		return "/admin/typeAmbient/typeAmbient";
	}

	@RequestMapping(value = "/findTypeAmbient/{id}", method = RequestMethod.GET)
	public @ResponseBody TypeAmbient findStatus(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		try {
			TypeAmbient typeAmbient = typeAmbientService.findById(id);
			return typeAmbient;
		} catch (Exception e) {
			Sentry.capture(e, "typeAmbient");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}

	@RequestMapping(path = "/saveTypeAmbient", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveStatus(HttpServletRequest request,

			@Valid @ModelAttribute("TypeAmbient") TypeAmbient typeAmbient, BindingResult errors, ModelMap model,
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
			if (res.getStatus().equals("success")) {
				typeAmbient.setId(null);
				typeAmbientService.save(typeAmbient);
				res.setObj(typeAmbient);
			}
		} catch (Exception e) {
			Sentry.capture(e, "typeAmbient");
			res.setStatus("exception");
			res.setException("Error al crear tipo de ambiente: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/updateTypeAmbient", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateStatus(HttpServletRequest request,
			@Valid @ModelAttribute("TypeAmbient") TypeAmbient typeAmbient, BindingResult errors, ModelMap model,
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
			if (res.getStatus().equals("success")) {
				TypeAmbient typeStatusOrigin = typeAmbientService.findById(typeAmbient.getId());
				typeStatusOrigin.setName(typeAmbient.getName());
				typeStatusOrigin.setDescription(typeAmbient.getDescription());
				typeAmbientService.update(typeStatusOrigin);
				res.setObj(typeAmbient);
			}
		} catch (Exception e) {
			Sentry.capture(e, "typeAmbient");
			res.setStatus("exception");
			res.setException("Error al modificar tipo de ambiente: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteTypeAmbient/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteStatus(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			typeAmbientService.delete(id);
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar tipo de ambiente: " + e.getCause().getCause().getCause().getMessage()
					+ ":" + e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar ambiente: Existen referencias que debe eliminar antes");
			} else {
				Sentry.capture(e, "typeAmbient");
			}

			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}

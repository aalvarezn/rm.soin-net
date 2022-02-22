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
import com.soin.sgrm.model.TypeRequest;
import com.soin.sgrm.service.TypeRequestService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/typeRequest")
public class TypeRequestController extends BaseController {

	public static final Logger logger = Logger.getLogger(TypeRequestController.class);

	@Autowired
	TypeRequestService typeRequestService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("typeRequests", typeRequestService.list());
		model.addAttribute("typeRequest", new TypeRequest());
		return "/admin/typeRequest/typeRequest";
	}

	@RequestMapping(value = "/findTypeRequest/{id}", method = RequestMethod.GET)
	public @ResponseBody TypeRequest findTypeRequest(@PathVariable Integer id, HttpServletRequest request,
			Locale locale, Model model, HttpSession session) {
		try {
			TypeRequest typeRequest = typeRequestService.findById(id);
			return typeRequest;
		} catch (Exception e) {
			Sentry.capture(e, "typeRequest");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}

	@RequestMapping(path = "/saveTypeRequest", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveTypeRequest(HttpServletRequest request,

			@Valid @ModelAttribute("TypeRequest") TypeRequest typeRequest, BindingResult errors, ModelMap model,
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
				typeRequestService.save(typeRequest);
				res.setObj(typeRequest);
			}
		} catch (Exception e) {
			Sentry.capture(e, "typeRequest");
			res.setStatus("exception");
			res.setException("Error al crear tipo de requerimiento: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/updateTypeRequest", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateTypeRequest(HttpServletRequest request,
			@Valid @ModelAttribute("TypeRequest") TypeRequest typeRequest, BindingResult errors, ModelMap model,
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
				TypeRequest typeRequestOrigin = typeRequestService.findById(typeRequest.getId());
				typeRequestOrigin.setCode(typeRequest.getCode());
				typeRequestOrigin.setDescription(typeRequest.getDescription());
				typeRequestService.update(typeRequestOrigin);
				res.setObj(typeRequest);
			}
		} catch (Exception e) {
			Sentry.capture(e, "typeRequest");
			res.setStatus("exception");
			res.setException("Error al modificar tipo de requerimiento: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteTypeRequest/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteTypeRequest(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			typeRequestService.delete(id);
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar tipo de requerimiento: "
					+ e.getCause().getCause().getCause().getMessage() + ":" + e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException(
						"Error al eliminar tipo de requerimiento: Existen referencias que debe eliminar antes");
			} else {
				Sentry.capture(e, "typeRequest");
			}

			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}

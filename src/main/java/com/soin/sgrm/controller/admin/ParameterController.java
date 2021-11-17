package com.soin.sgrm.controller.admin;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
import com.soin.sgrm.model.Parameter;
import com.soin.sgrm.service.ParameterService;
import com.soin.sgrm.utils.JsonResponse;

@Controller
@RequestMapping("/admin/parameter")
public class ParameterController extends BaseController {

	@Autowired
	ParameterService parameterService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("parameters", parameterService.listAll());
		return "/admin/parameter/parameter";
	}

	@RequestMapping(value = "/findParameter/{id}", method = RequestMethod.GET)
	public @ResponseBody Parameter findParameter(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		try {
			Parameter param = parameterService.findById(id);
			return param;
		} catch (Exception e) {
			logs("RELEASE_ERROR", "Error findParameter. " + getErrorFormat(e));
			return null;
		}
	}

	@RequestMapping(value = "/updateParameter", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateParameter(HttpServletRequest request,
			@Valid @ModelAttribute("Parameter") Parameter param, BindingResult errors, ModelMap model, Locale locale,
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
			if (res.getStatus().equals("success")) {
				Parameter parameter = parameterService.findById(param.getId());
				parameter.setDescription(param.getDescription());
				parameter.setParamValue(param.getParamValue());
				parameter.setDate(getSystemTimestamp());
				parameterService.updateParameter(parameter);
				res.setObj(parameter);
			}

		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al modificar parámetro: " + e.toString());
			logs("ADMIN_ERROR", "Error al modificar parámetro: " + getErrorFormat(e));
		}
		return res;
	}

}

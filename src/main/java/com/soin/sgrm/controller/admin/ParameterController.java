package com.soin.sgrm.controller.admin;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
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
import org.springframework.beans.factory.annotation.Autowired;

import com.soin.sgrm.controller.BaseController;

import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.pos.PParameter;
import com.soin.sgrm.service.pos.ParameterService;

@Controller
@RequestMapping("/admin/parameter")
public class ParameterController extends BaseController {

	public static final Logger logger = Logger.getLogger(ParameterController.class);

	@Autowired
	ParameterService parameterService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("parameters", parameterService.findAll());
		return "/admin/parameter/parameter";
	}

	@RequestMapping(value = "/findParameter/{id}", method = RequestMethod.GET)
	public @ResponseBody PParameter findParameter(@PathVariable Long id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		try {
			PParameter param = parameterService.findById(id);
			return param;
		} catch (Exception e) {
			Sentry.capture(e, "parameter");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}

	@RequestMapping(value = "/updateParameter", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateParameter(HttpServletRequest request,
			@Valid @ModelAttribute("Parameter") PParameter param, BindingResult errors, ModelMap model, Locale locale,
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
				PParameter parameter = parameterService.findById(param.getId());
				parameter.setDescription(param.getDescription());
				parameter.setParamValue(param.getParamValue());
				parameter.setDate(CommonUtils.getSystemTimestamp());
				parameterService.update(parameter);
				res.setObj(parameter);
			}

		} catch (Exception e) {
			Sentry.capture(e, "parameter");
			res.setStatus("exception");
			res.setException("Error al modificar parámetro: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}

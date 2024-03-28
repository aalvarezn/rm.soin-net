package com.soin.sgrm.controller.admin;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import com.soin.sgrm.model.pos.PParameter;
import com.soin.sgrm.service.ParameterService;
import com.soin.sgrm.service.pos.PParameterService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/parameter")
public class ParameterController extends BaseController {

	public static final Logger logger = Logger.getLogger(ParameterController.class);

	@Autowired
	ParameterService parameterService;
	
	@Autowired
	PParameterService pparameterService;

	private final Environment environment;

	@Autowired
	public ParameterController(Environment environment) {
		this.environment = environment;
	}

	public String profileActive() {
		String[] activeProfiles = environment.getActiveProfiles();
		for (String profile : activeProfiles) {
			return profile;
		}
		return "";
	}
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		
		String profile = profileActive();
		if (profile.equals("oracle")) {
			model.addAttribute("parameters", parameterService.listAll());
		} else if (profile.equals("postgres")) {
			model.addAttribute("parameters", pparameterService.listAll());
		}
		
		return "/admin/parameter/parameter";
	}

	@RequestMapping(value = "/findParameter/{id}", method = RequestMethod.GET)
	public @ResponseBody Parameter findParameter(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		try {
			String profile = profileActive();
			Parameter param = new Parameter();
			if (profile.equals("oracle")) {
				 param = parameterService.findById(id);
			} else if (profile.equals("postgres")) {
				PParameter pparam = pparameterService.findById(id);
				param.setId(pparam.getId());
				param.setCode(pparam.getCode());
				param.setDate(pparam.getDate());
				param.setDescription(pparam.getDescription());
				param.setParamValue(pparam.getParamValue());
			}
			
			return param;
		} catch (Exception e) {
			Sentry.capture(e, "parameter");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
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
				
				String profile = profileActive();
				if (profile.equals("oracle")) {
					Parameter parameter = parameterService.findById(param.getId());
					parameter.setDescription(param.getDescription());
					parameter.setParamValue(param.getParamValue());
					parameter.setDate(CommonUtils.getSystemTimestamp());
					parameterService.updateParameter(parameter);
					res.setObj(parameter);
				} else if (profile.equals("postgres")) {
					PParameter pparameter = pparameterService.findById(param.getId());
					pparameter.setDescription(param.getDescription());
					pparameter.setParamValue(param.getParamValue());
					pparameter.setDate(CommonUtils.getSystemTimestamp());
					pparameterService.updateParameter(pparameter);
					res.setObj(pparameter);
				}
				
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

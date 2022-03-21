package com.soin.sgrm.controller.admin;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.pos.ParameterService;

@Controller
@RequestMapping("/admin/parameter")
public class ParameterController extends BaseController {

	public static final Logger logger = Logger.getLogger(ParameterController.class);

	@Autowired
	ParameterService parameterService;


	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		return "/admin/parameter/parameter";
	}
	


	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<PParameter> rfcs = new JsonSheet<>();
		try {
			rfcs.setData(parameterService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rfcs;
	}
	
	
	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody PParameter uptParameter) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			uptParameter.setDate(CommonUtils.getSystemTimestamp());
			parameterService.update(uptParameter);

			res.setMessage("Parametro modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "parameter");
			res.setStatus("exception");
			res.setMessage("Error al modificar parametro!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	

}

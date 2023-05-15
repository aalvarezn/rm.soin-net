package com.soin.sgrm.controller;

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

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.StatusKnowlege;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.StatusKnowlegeService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/statusKnowledge")
public class StatusKnowlegeController extends BaseController {
	public static final Logger logger = Logger.getLogger(StatusKnowlegeController.class);

	@Autowired
	StatusKnowlegeService statusKnowlegeService;
	
	
	
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {

		return "/incidence/knowledge/status/statusKnowledge";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<StatusKnowlege> StatusKnowleges = new JsonSheet<>();
		try {
			StatusKnowleges.setData(statusKnowlegeService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return StatusKnowleges;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody StatusKnowlege addStatusKnowlege) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");

			statusKnowlegeService.save(addStatusKnowlege);

			res.setMessage("Estado agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "StatusKnowlege");
			res.setStatus("error");
			res.setMessage("Error al agregar estado Request!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody StatusKnowlege uptStatusKnowlege) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			statusKnowlegeService.update(uptStatusKnowlege);

			res.setMessage("Estado modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "StatusKnowlege");
			res.setStatus("error");
			res.setMessage("Error al modificar estado!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			statusKnowlegeService.delete(id);
			res.setMessage("Estado eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "StatusKnowlege");
			res.setStatus("error");
			res.setMessage("Error al eliminar el estado de solicitud!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}
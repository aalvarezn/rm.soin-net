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

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.model.pos.PAmbient;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.pos.AmbientService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/ambient")
public class AmbientController extends BaseController {
	
	public static final Logger logger = Logger.getLogger(AmbientController.class);

	@Autowired
	AmbientService ambientService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		return "/admin/ambient/ambient";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<PAmbient> rfcs = new JsonSheet<>();
		try {
			rfcs.setData(ambientService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rfcs;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody PAmbient addAmbient) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			addAmbient.setCode(addAmbient.getCode().toUpperCase());
			ambientService.save(addAmbient);

			res.setMessage("Ambiente agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "ambient");
			res.setStatus("exception");
			res.setMessage("Error al agregar ambiente!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody PAmbient uptAmbient) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			ambientService.update(uptAmbient);

			res.setMessage("Ambiente modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "ambient");
			res.setStatus("exception");
			res.setMessage("Error al modificar ambiente!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			ambientService.delete(id);
			res.setMessage("Ambiente eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "ambient");
			res.setStatus("exception");
			res.setMessage("Error al eliminar el ambiente!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}


}

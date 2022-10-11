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
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Errors;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.ErrorService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/error")
public class ErrorsController extends BaseController {
	public static final Logger logger = Logger.getLogger(ErrorsController.class);

	@Autowired
	ErrorService errorService;
	
	
	
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {

		return "/admin/errors/errors";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<Errors> statusRFCs = new JsonSheet<>();
		try {
			statusRFCs.setData(errorService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return statusRFCs;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody Errors addError) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");

			errorService.save(addError);

			res.setMessage("Error agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "Errors");
			res.setStatus("exception");
			res.setMessage("Error al agregar Error!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody Errors uptError) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			errorService.update(uptError);

			res.setMessage("Error modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "Errors");
			res.setStatus("exception");
			res.setMessage("Error al modificar Error!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			errorService.delete(id);
			res.setMessage("Error eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "errors");
			res.setStatus("exception");
			res.setMessage("Error al eliminar el Error!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}

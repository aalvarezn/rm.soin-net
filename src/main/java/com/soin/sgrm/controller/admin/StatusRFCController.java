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
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.StatusRFCService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/statusRFC")
public class StatusRFCController extends BaseController {
	public static final Logger logger = Logger.getLogger(AmbientController.class);

	@Autowired
	StatusRFCService statusRFCService;
	
	
	
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {

		return "/admin/statusRFC/statusRFC";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<StatusRFC> statusRFCs = new JsonSheet<>();
		try {
			statusRFCs.setData(statusRFCService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return statusRFCs;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody StatusRFC addStatusRFC) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");

			statusRFCService.save(addStatusRFC);

			res.setMessage("Status RFC agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "statusRFC");
			res.setStatus("exception");
			res.setMessage("Error al agregar Status RFC!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody StatusRFC uptStatusRFC) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			statusRFCService.update(uptStatusRFC);

			res.setMessage("Status RFC modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "statusRFC");
			res.setStatus("exception");
			res.setMessage("Error al modificar Status RFC!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			statusRFCService.delete(id);
			res.setMessage("Status RFC eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "siges");
			res.setStatus("exception");
			res.setMessage("Error al eliminar el Status RFC!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}

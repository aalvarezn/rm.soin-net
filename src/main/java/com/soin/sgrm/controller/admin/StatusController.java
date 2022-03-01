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
import com.soin.sgrm.model.pos.PStatus;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.pos.StatusService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/status")
public class StatusController extends BaseController {

	public static final Logger logger = Logger.getLogger(StatusController.class);

	@Autowired
	StatusService status;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		return "/admin/status/status";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<PStatus> list = new JsonSheet<>();
		try {
			list.setData(status.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveStatus(HttpServletRequest request, @RequestBody PStatus addStatus) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			status.save(addStatus);

			res.setMessage("Estado agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "status");
			res.setStatus("exception");
			res.setMessage("Error al agregar estado!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse updateStatus(HttpServletRequest request, @RequestBody PStatus uptStatus) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			status.update(uptStatus);

			res.setMessage("Estado modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "status");
			res.setStatus("exception");
			res.setMessage("Error al modificar estado!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteStatus(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			status.delete(id);
			res.setMessage("Estado eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "status");
			res.setStatus("exception");
			res.setMessage("Error al eliminar estado!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}

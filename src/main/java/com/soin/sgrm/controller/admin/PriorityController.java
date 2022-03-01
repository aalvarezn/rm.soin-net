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
import com.soin.sgrm.model.pos.PPriority;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.pos.PriorityService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/priority")
public class PriorityController extends BaseController {

	public static final Logger logger = Logger.getLogger(PriorityController.class);

	@Autowired
	PriorityService priority;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		return "/admin/priority/priority";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<PPriority> list = new JsonSheet<>();
		try {
			list.setData(priority.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody PPriority addPriority) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			priority.save(addPriority);

			res.setMessage("Prioridad agregada!");
		} catch (Exception e) {
			Sentry.capture(e, "priority");
			res.setStatus("exception");
			res.setMessage("Error al agregar prioridad!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody PPriority uptPriority) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			priority.update(uptPriority);

			res.setMessage("Prioridad modificada!");
		} catch (Exception e) {
			Sentry.capture(e, "priority");
			res.setStatus("exception");
			res.setMessage("Error al modificar prioridad!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			priority.delete(id);
			res.setMessage("Priridad eliminada!");
		} catch (Exception e) {
			Sentry.capture(e, "priority");
			res.setStatus("exception");
			res.setMessage("Error al eliminar prioridad!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}

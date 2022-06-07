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
import com.soin.sgrm.model.pos.PAction;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.ActionEnvironmentService;
import com.soin.sgrm.service.pos.ActionService;
import com.soin.sgrm.service.pos.SystemService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/action")
public class ActionEnvironmentController extends BaseController {

	public static final Logger logger = Logger.getLogger(ActionEnvironmentController.class);

	@Autowired
	ActionEnvironmentService actionEnvironmentService;
	
	@Autowired
	ActionService actionService;
	
	 @Autowired
	 SystemService  systemService;

	
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("systems",systemService.findAll());
		
		return "/admin/actionEnvironment/actionEnvironment";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<PAction> actions = new JsonSheet<>();
		try {
			actions.setData(actionService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return actions;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody PAction addAction) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			PSystem system=systemService.findById(addAction.getSystemId());
			addAction.setSystem(system);
			actionService.save(addAction);

			res.setMessage("Accion agregada!");
		} catch (Exception e) {
			Sentry.capture(e, "action");
			res.setStatus("exception");
			res.setMessage("Error al agregar Accion!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody PAction uptAction) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			PSystem system=systemService.findById(uptAction.getSystemId());
			uptAction.setSystem(system);
			actionService.update(uptAction);

			res.setMessage("Accion modificada!");
		} catch (Exception e) {
			Sentry.capture(e, "action");
			res.setStatus("exception");
			res.setMessage("Error al modificar Accion!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			actionService.delete(id);
			res.setMessage("Accion eliminada!");
		} catch (Exception e) {
			Sentry.capture(e, "action");
			res.setStatus("exception");
			res.setMessage("Error al eliminar accion!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	
}

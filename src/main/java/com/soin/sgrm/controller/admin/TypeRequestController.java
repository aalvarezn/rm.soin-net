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
import com.soin.sgrm.model.pos.PTypeRequest;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.pos.TypeRequestService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/typeRequest")
public class TypeRequestController extends BaseController {

	public static final Logger logger = Logger.getLogger(TypeRequestController.class);

	@Autowired
	TypeRequestService typeRequestService;
	
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		return "/admin/typeRequest/typeRequest";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<PTypeRequest> rfcs = new JsonSheet<>();
		try {
			rfcs.setData(typeRequestService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rfcs;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody PTypeRequest addTypeRequest) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			typeRequestService.save(addTypeRequest);

			res.setMessage("Tipo de requrimiento agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "typeRequest");
			res.setStatus("exception");
			res.setMessage("Error al agregar tipo de requerimiento!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody PTypeRequest uptTypeRequest) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			typeRequestService.update(uptTypeRequest);

			res.setMessage("Tipo de requerimiento modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "typeRequest");
			res.setStatus("exception");
			res.setMessage("Error al modificar tipo de requerimiento!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			typeRequestService.delete(id);
			res.setMessage("Tipo de requerimiento eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "typeRequest");
			res.setStatus("exception");
			res.setMessage("Error al eliminar tipo de requerimiento!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}

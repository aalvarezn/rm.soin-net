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
import com.soin.sgrm.model.TypeChange;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.TypeChangeService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/typeChange")
public class TypeChangeController extends BaseController {
	public static final Logger logger = Logger.getLogger(TypeChangeController.class);

	@Autowired
	TypeChangeService typeChangeService;
	
	
	
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {

		return "/admin/typeChange/typeChange";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<TypeChange> typeChange = new JsonSheet<>();
		try {
			typeChange.setData(typeChangeService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return typeChange;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody TypeChange addTypeChange) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");

			typeChangeService.save(addTypeChange);

			res.setMessage("Tipo cambio agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "typeChange");
			res.setStatus("exception");
			res.setMessage("Error al agregar Tipo cambio!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody TypeChange uptTypeChange) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			typeChangeService.update(uptTypeChange);

			res.setMessage("Tipo cambio modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "typeChange");
			res.setStatus("exception");
			res.setMessage("Error al modificar Tipo cambio!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			typeChangeService.delete(id);
			res.setMessage("Tipo cambio  eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "typeChange");
			res.setStatus("exception");
			res.setMessage("Error al eliminar el Tipo cambio!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}

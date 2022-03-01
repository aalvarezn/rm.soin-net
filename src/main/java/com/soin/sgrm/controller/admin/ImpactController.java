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
import com.soin.sgrm.model.pos.PImpact;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.pos.ImpactService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/impact")
public class ImpactController extends BaseController {

	public static final Logger logger = Logger.getLogger(ImpactController.class);

	@Autowired
	ImpactService impact;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		return "/admin/impact/impact";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<PImpact> rfcs = new JsonSheet<>();
		try {
			rfcs.setData(impact.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rfcs;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody PImpact addImpact) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			impact.save(addImpact);

			res.setMessage("Impacto agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "impact");
			res.setStatus("exception");
			res.setMessage("Error al agregar impacto!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody PImpact uptImpact) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			impact.update(uptImpact);

			res.setMessage("Impacto modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "impact");
			res.setStatus("exception");
			res.setMessage("Error al modificar impacto!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			impact.delete(id);
			res.setMessage("Impacto eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "impact");
			res.setStatus("exception");
			res.setMessage("Error al eliminar impacto!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}

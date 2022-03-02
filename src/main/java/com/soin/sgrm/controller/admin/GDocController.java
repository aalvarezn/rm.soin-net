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
import com.soin.sgrm.model.pos.PGDocConfiguration;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.service.pos.GDocConfigurationService;
import com.soin.sgrm.service.pos.ProjectService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/gDoc")
public class GDocController extends BaseController {

	public static final Logger logger = Logger.getLogger(GDocController.class);

	@Autowired
	GDocConfigurationService gDocService;

	@Autowired
	ProjectService projectService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("projects", projectService.findAll());
		model.addAttribute("project", new Project());
		return "/admin/gDoc/gDoc";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<PGDocConfiguration> rfcs = new JsonSheet<>();
		try {
			rfcs.setData(gDocService.findAll());
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rfcs;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody PGDocConfiguration addGDoc) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			gDocService.save(addGDoc);

			res.setMessage("Configuracion agregada!");
		} catch (Exception e) {
			Sentry.capture(e, "gDocConfig");
			res.setStatus("exception");
			res.setMessage("Error al agregar configuracion!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody PGDocConfiguration uptGDoc) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			gDocService.update(uptGDoc);

			res.setMessage("Configuracion modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "gDocConfig");
			res.setStatus("exception");
			res.setMessage("Error al modificar configuracion!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			gDocService.delete(id);
			res.setMessage("Configuracion eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "gDocConfig");
			res.setStatus("exception");
			res.setMessage("Error al eliminar configuracion!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}

package com.soin.sgrm.controller.admin;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.model.GDoc;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.service.GDocService;
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/gDoc")
public class GDocController extends BaseController {

	public static final Logger logger = Logger.getLogger(GDocController.class);

	@Autowired
	GDocService gDocService;

	@Autowired
	ProjectService projectService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("gDocs", gDocService.list());
		model.addAttribute("gDoc", new GDoc());
		model.addAttribute("projects", projectService.listAll());
		model.addAttribute("project", new Project());
		return "/admin/gDoc/gDoc";
	}

	@RequestMapping(value = "/findGDoc/{id}", method = RequestMethod.GET)
	public @ResponseBody GDoc findGDoc(@PathVariable Integer id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
		try {
			GDoc gDoc = gDocService.findById(id);
			return gDoc;
		} catch (Exception e) {
			Sentry.capture(e, "gDocs");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}

	@RequestMapping(path = "/saveGDoc", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveGDoc(HttpServletRequest request,

			@Valid @ModelAttribute("GDoc") GDoc gDoc, BindingResult errors, ModelMap model, Locale locale,
			HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");

			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}

			if (gDoc.getProyectId() == null) {
				res.setStatus("fail");
				res.addError("proyectId", "Seleccione una opción");
			}

			if (res.getStatus().equals("success")) {
				gDoc.setProyect(projectService.findById(gDoc.getProyectId()));
				gDoc.setNextSincronization(CommonUtils.getSqlDate());
				gDocService.save(gDoc);
				res.setObj(gDoc);
			}
		} catch (Exception e) {
			Sentry.capture(e, "gDocs");
			res.setStatus("exception");
			res.setException("Error al crear gDoc: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/updateGDoc", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateGDoc(HttpServletRequest request, @Valid @ModelAttribute("GDoc") GDoc gDoc,
			BindingResult errors, ModelMap model, Locale locale, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}
			if (gDoc.getProyectId() == null) {
				res.setStatus("fail");
				res.addError("proyectId", "Seleccione una opción");
			}

			if (res.getStatus().equals("success")) {
				GDoc gDocOrigin = gDocService.findById(gDoc.getId());
				gDocOrigin.setCredentials(gDoc.getCredentials());
				gDocOrigin.setSpreadSheet(gDoc.getSpreadSheet());
				gDocOrigin.setDescription(gDoc.getDescription());
				gDocOrigin.setProyect(projectService.findById(gDoc.getProyectId()));
				gDocService.update(gDocOrigin);
				res.setObj(gDoc);
			}
		} catch (Exception e) {
			Sentry.capture(e, "gDocs");
			res.setStatus("exception");
			res.setException("Error al modificar gDoc: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteGDoc/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteGDoc(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			gDocService.delete(id);
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar gDoc: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar gDoc: Existen referencias que debe eliminar antes");
			} else {
				Sentry.capture(e, "gDocs");
			}
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}

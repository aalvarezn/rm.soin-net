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

import com.soin.sgrm.model.pos.PProject;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.pos.ProjectService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/project")
public class ProjectController extends BaseController {

	public static final Logger logger = Logger.getLogger(ProjectController.class);

	@Autowired
	ProjectService projectService;


	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		return "/admin/project/project";
	}
	


	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<PProject> rfcs = new JsonSheet<>();
		try {
			rfcs.setData(projectService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rfcs;
	}
	
	

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody PProject addProject) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			projectService.save(addProject);

			res.setMessage("Proyecto agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "project");
			res.setStatus("exception");
			res.setMessage("Error al agregar proyecto!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody PProject uptProject) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			projectService.update(uptProject);

			res.setMessage("Proyecto modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "project");
			res.setStatus("exception");
			res.setMessage("Error al modificar proyecto!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
	

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			projectService.delete(id);
			res.setMessage("Proyecto eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "project");
			res.setStatus("exception");
			res.setMessage("Error al eliminar proyecto!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	
}

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
import com.soin.sgrm.model.Project;
import com.soin.sgrm.service.ProjectService;
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
		model.addAttribute("projects", projectService.listAll());
		model.addAttribute("project", new Project());
		return "/admin/project/project";
	}

	@RequestMapping(value = "/findProject/{id}", method = RequestMethod.GET)
	public @ResponseBody Project findProject(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		try {
			Project project = projectService.findById(id);
			return project;
		} catch (Exception e) {
			Sentry.capture(e, "project");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}

	@RequestMapping(path = "/saveProject", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveProject(HttpServletRequest request,

			@Valid @ModelAttribute("Project") Project project, BindingResult errors, ModelMap model, Locale locale,
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
			if (res.getStatus().equals("success")) {
				project.setNotify(false);
				project.setNotifyManager(false);
				project.setNotifyPMO(false);
				project.setNotifyTechLead(false);
				projectService.save(project);
				res.setObj(project);
			}
		} catch (Exception e) {
			Sentry.capture(e, "project");
			res.setStatus("exception");
			res.setException("Error al crear proyecto: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/updateProject", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateProject(HttpServletRequest request,
			@Valid @ModelAttribute("Project") Project project, BindingResult errors, ModelMap model, Locale locale,
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
			if (res.getStatus().equals("success")) {
				Project proj = projectService.findById(project.getId());
				proj.setCode(project.getCode());
				proj.setDescription(project.getDescription());
				proj.setAllowRepeat(project.getAllowRepeat());
				projectService.update(proj);
				res.setObj(project);
			}
		} catch (Exception e) {
			Sentry.capture(e, "project");
			res.setStatus("exception");
			res.setException("Error al modificar proyecto: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteProject/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteProject(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			projectService.delete(id);
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar proyecto: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar proyecto: Existen referencias que debe eliminar antes");
			} else {
				Sentry.capture(e, "project");
			}
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}

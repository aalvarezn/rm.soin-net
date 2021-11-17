package com.soin.sgrm.controller.admin;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
import com.soin.sgrm.model.Priority;
import com.soin.sgrm.service.PriorityService;
import com.soin.sgrm.utils.JsonResponse;

@Controller
@RequestMapping("/admin/priority")
public class PriorityController extends BaseController {

	@Autowired
	PriorityService priorityService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("prioritys", priorityService.list());
		model.addAttribute("priority", new Priority());
		return "/admin/priority/priority";
	}

	@RequestMapping(value = "/findPriority/{id}", method = RequestMethod.GET)
	public @ResponseBody Priority findPriority(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		try {
			Priority priority = priorityService.findById(id);
			return priority;
		} catch (Exception e) {
			logs("ADMIN_ERROR", "Error findPriority. " + getErrorFormat(e));
			return null;
		}
	}

	@RequestMapping(path = "/savePriority", method = RequestMethod.POST)
	public @ResponseBody JsonResponse savePriority(HttpServletRequest request,

			@Valid @ModelAttribute("Priority") Priority priority, BindingResult errors, ModelMap model, Locale locale,
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
				priorityService.save(priority);
				res.setObj(priority);
			}
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al crear prioridad: " + e.toString());
			logs("ADMIN_ERROR", "Error al crear prioridad: " + getErrorFormat(e));
			e.printStackTrace();
		}
		return res;
	}

	@RequestMapping(value = "/updatePriority", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updatePriority(HttpServletRequest request,
			@Valid @ModelAttribute("Priority") Priority priority, BindingResult errors, ModelMap model, Locale locale,
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
				Priority priorityOrigin = priorityService.findById(priority.getId());
				priorityOrigin.setName(priority.getName());
				priorityOrigin.setDescription(priority.getDescription());
				priorityService.update(priorityOrigin);
				res.setObj(priority);
			}
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al modificar prioridad: " + e.toString());
			logs("ADMIN_ERROR", "Error al modificar prioridad: " + getErrorFormat(e));
		}
		return res;
	}

	@RequestMapping(value = "/deletePriority/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deletePriority(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			priorityService.delete(id);
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar prioridad: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());
			logs("ADMIN_ERROR", "Error al eliminar prioridad: " + getErrorFormat(e));
			e.printStackTrace();
		}
		return res;
	}

}

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
import com.soin.sgrm.model.Status;
import com.soin.sgrm.service.StatusService;
import com.soin.sgrm.utils.JsonResponse;

import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/status")
public class StatusController extends BaseController{
	
	@Autowired
	StatusService statusService;
	
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("statuses", statusService.list());
		model.addAttribute("status", new Status());
		return "/admin/status/status";
	}
	
	@RequestMapping(value = "/findStatus/{id}", method = RequestMethod.GET)
	public @ResponseBody Status findStatus(@PathVariable Integer id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
		try {
			Status status = statusService.findById(id);
			return status;
		} catch (Exception e) {
			Sentry.capture(e, "status");
			return null;
		}
	}

	@RequestMapping(path = "/saveStatus", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveStatus(HttpServletRequest request,

			@Valid @ModelAttribute("Status") Status status, BindingResult errors, ModelMap model, Locale locale,
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
				status.setFinished(false);
				status.setInProgress(false);
				statusService.save(status);
				res.setObj(status);
			}
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al crear estado: " + e.toString());
			Sentry.capture(e, "status");
		}
		return res;
	}

	@RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateStatus(HttpServletRequest request, @Valid @ModelAttribute("Status") Status status,
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
			if (res.getStatus().equals("success")) {
				Status statusOrigin = statusService.findById(status.getId());
				statusOrigin.setName(status.getName());
				statusOrigin.setDescription(status.getDescription());
				statusService.update(statusOrigin);
				res.setObj(status);
			}
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al modificar estado: " + e.toString());
			Sentry.capture(e, "status");
		}
		return res;
	}

	@RequestMapping(value = "/deleteStatus/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteStatus(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			statusService.delete(id);
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar estado: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());
			
			if(e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar estado: Existen referencias que debe eliminar antes");
			}else {
				Sentry.capture(e, "status");
			}
		}
		return res;
	}
}

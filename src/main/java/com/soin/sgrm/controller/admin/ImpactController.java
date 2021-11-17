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
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.service.ImpactService;
import com.soin.sgrm.utils.JsonResponse;

@Controller
@RequestMapping("/admin/impact")
public class ImpactController extends BaseController {

	@Autowired
	ImpactService impactService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("impacts", impactService.list());
		model.addAttribute("impact", new Impact());
		return "/admin/impact/impact";
	}

	@RequestMapping(value = "/findImpact/{id}", method = RequestMethod.GET)
	public @ResponseBody Impact findImpact(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		try {
			Impact impact = impactService.findById(id);
			return impact;
		} catch (Exception e) {
			logs("ADMIN_ERROR", "Error findImpact. " + getErrorFormat(e));
			return null;
		}
	}

	@RequestMapping(path = "/saveImpact", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveImpact(HttpServletRequest request,

			@Valid @ModelAttribute("Impact") Impact impact, BindingResult errors, ModelMap model, Locale locale,
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
				impactService.save(impact);
				res.setObj(impact);
			}
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al crear impacto: " + e.toString());
			logs("ADMIN_ERROR", "Error al crear impacto: " + getErrorFormat(e));
			e.printStackTrace();
		}
		return res;
	}

	@RequestMapping(value = "/updateImpact", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateImpact(HttpServletRequest request,
			@Valid @ModelAttribute("Impact") Impact impact, BindingResult errors, ModelMap model, Locale locale,
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
				Impact impactOrigin = impactService.findById(impact.getId());
				impactOrigin.setName(impact.getName());
				impactOrigin.setDescription(impact.getDescription());
				impactService.update(impactOrigin);
				res.setObj(impact);
			}
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al modificar impacto: " + e.toString());
			logs("ADMIN_ERROR", "Error al modificar impacto: " + getErrorFormat(e));
		}
		return res;
	}

	@RequestMapping(value = "/deleteImpact/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteImpact(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			impactService.delete(id);
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar impacto: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());
			logs("ADMIN_ERROR", "Error al eliminar impacto: " + getErrorFormat(e));
			e.printStackTrace();
		}
		return res;
	}

}

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
import com.soin.sgrm.model.Risk;
import com.soin.sgrm.service.RiskService;
import com.soin.sgrm.utils.JsonResponse;

@Controller
@RequestMapping("/admin/risk")
public class RiskController extends BaseController {

	@Autowired
	RiskService riskService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("risks", riskService.list());
		model.addAttribute("risk", new Risk());
		return "/admin/risk/risk";
	}

	@RequestMapping(value = "/findRisk/{id}", method = RequestMethod.GET)
	public @ResponseBody Risk findRisk(@PathVariable Integer id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
		try {
			Risk risk = riskService.findById(id);
			return risk;
		} catch (Exception e) {
			logs("ADMIN_ERROR", "Error findRisk. " + getErrorFormat(e));
			return null;
		}
	}

	@RequestMapping(path = "/saveRisk", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveRisk(HttpServletRequest request,

			@Valid @ModelAttribute("Risk") Risk risk, BindingResult errors, ModelMap model, Locale locale,
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
				riskService.save(risk);
				res.setObj(risk);
			}
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al crear riesgo: " + e.toString());
			logs("ADMIN_ERROR", "Error al crear riesgo: " + getErrorFormat(e));
			e.printStackTrace();
		}
		return res;
	}

	@RequestMapping(value = "/updateRisk", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateRisk(HttpServletRequest request, @Valid @ModelAttribute("Risk") Risk risk,
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
				Risk riskOrigin = riskService.findById(risk.getId());
				riskOrigin.setName(risk.getName());
				riskOrigin.setDescription(risk.getDescription());
				riskService.update(riskOrigin);
				res.setObj(risk);
			}
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al modificar riesgo: " + e.toString());
			logs("ADMIN_ERROR", "Error al modificar riesgo: " + getErrorFormat(e));
		}
		return res;
	}

	@RequestMapping(value = "/deleteRisk/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteRisk(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			riskService.delete(id);
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar riesgo: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());
			logs("ADMIN_ERROR", "Error al eliminar riesgo: " + getErrorFormat(e));
			e.printStackTrace();
		}
		return res;
	}

}

package com.soin.sgrm.controller.admin;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import com.soin.sgrm.model.pos.PRisk;
import com.soin.sgrm.service.RiskService;
import com.soin.sgrm.service.pos.PRiskService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/risk")
public class RiskController extends BaseController {

	public static final Logger logger = Logger.getLogger(RiskController.class);

	@Autowired
	RiskService riskService;
	@Autowired
	PRiskService priskService;
	
	
	private final Environment environment;

	@Autowired
	public RiskController(Environment environment) {
		this.environment = environment;
	}

	public String profileActive() {
		String[] activeProfiles = environment.getActiveProfiles();
		 for (String profile : activeProfiles) {
			 return profile;
	        }
		return "";
	}
	
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		String profile = profileActive();
		if (profile.equals("oracle")) {
			model.addAttribute("risks", riskService.list());
			model.addAttribute("risk", new Risk());
		} else if (profile.equals("postgres")) {
			model.addAttribute("risks", priskService.list());
			model.addAttribute("risk", new PRisk());
		}
		
		
		return "/admin/risk/risk";
	}

	@RequestMapping(value = "/findRisk/{id}", method = RequestMethod.GET)
	public @ResponseBody Object findRisk(@PathVariable Integer id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
		try {
			String profile = profileActive();
			if (profile.equals("oracle")) {
				Risk risk = riskService.findById(id);
				return risk;
			} else if (profile.equals("postgres")) {
				PRisk risk = priskService.findById(id);
				return risk;
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "risk");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
		return null;
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
				String profile = profileActive();
				if (profile.equals("oracle")) {
					riskService.save(risk);
					res.setObj(risk);
				} else if (profile.equals("postgres")) {
					PRisk prisk=new PRisk();
					prisk.setDescription(risk.getDescription());
					prisk.setName(risk.getName());
					priskService.save(prisk);
					res.setObj(prisk);
				}

			}
		} catch (Exception e) {
			Sentry.capture(e, "risk");
			res.setStatus("exception");
			res.setException("Error al crear riesgo: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
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
				
				String profile = profileActive();
				if (profile.equals("oracle")) {
					Risk riskOrigin = riskService.findById(risk.getId());
					riskOrigin.setName(risk.getName());
					riskOrigin.setDescription(risk.getDescription());
					riskService.update(riskOrigin);
					res.setObj(risk);
				} else if (profile.equals("postgres")) {
					PRisk priskOrigin = priskService.findById(risk.getId());
					priskOrigin.setName(risk.getName());
					priskOrigin.setDescription(risk.getDescription());
					priskOrigin.setId(risk.getId());
					priskService.update(priskOrigin);
					res.setObj(priskOrigin);
				}
				
			}
		} catch (Exception e) {
			Sentry.capture(e, "risk");
			res.setStatus("exception");
			res.setException("Error al modificar riesgo: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteRisk/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteRisk(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			String profile = profileActive();
			if (profile.equals("oracle")) {
				riskService.delete(id);
			} else if (profile.equals("postgres")) {
				priskService.delete(id);
			}
		
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar riesgo: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar riesgo: Existen referencias que debe eliminar antes");
			} else {
				Sentry.capture(e, "risk");
			}
		}
		return res;
	}

}

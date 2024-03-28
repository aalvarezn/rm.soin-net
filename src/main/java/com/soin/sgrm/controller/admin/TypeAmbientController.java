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
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.TypeAmbient;
import com.soin.sgrm.model.pos.PImpact;
import com.soin.sgrm.model.pos.PTypeAmbient;
import com.soin.sgrm.service.TypeAmbientService;
import com.soin.sgrm.service.pos.PTypeAmbientService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/typeAmbient")
public class TypeAmbientController extends BaseController {

	public static final Logger logger = Logger.getLogger(TypeAmbientController.class);

	@Autowired
	TypeAmbientService typeAmbientService;

	@Autowired
	PTypeAmbientService ptypeAmbientService;

	private final Environment environment;

	@Autowired
	public TypeAmbientController(Environment environment) {
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
			model.addAttribute("typeAmbients", typeAmbientService.list());
			model.addAttribute("typeAmbient", new TypeAmbient());
		} else if (profile.equals("postgres")) {
			model.addAttribute("typeAmbients", ptypeAmbientService.list());
			model.addAttribute("typeAmbient", new PImpact());
		}

		return "/admin/typeAmbient/typeAmbient";
	}

	@RequestMapping(value = "/findTypeAmbient/{id}", method = RequestMethod.GET)
	public @ResponseBody TypeAmbient findStatus(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		try {
			TypeAmbient typeAmbient = new TypeAmbient();
			String profile = profileActive();
			if (profile.equals("oracle")) {
				typeAmbient = typeAmbientService.findById(id);
			} else if (profile.equals("postgres")) {
				PTypeAmbient ptypeAmbient = ptypeAmbientService.findById(id);
				typeAmbient.setDescription(ptypeAmbient.getDescription());
				typeAmbient.setId(ptypeAmbient.getId());
				typeAmbient.setName(ptypeAmbient.getName());
			}

			return typeAmbient;
		} catch (Exception e) {
			Sentry.capture(e, "typeAmbient");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}

	@RequestMapping(path = "/saveTypeAmbient", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveStatus(HttpServletRequest request,

			@Valid @ModelAttribute("TypeAmbient") TypeAmbient typeAmbient, BindingResult errors, ModelMap model,
			Locale locale, HttpSession session) {
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
					typeAmbientService.save(typeAmbient);
				} else if (profile.equals("postgres")) {
					PTypeAmbient ptypeAmbient = new PTypeAmbient();
					ptypeAmbient.setName(typeAmbient.getName());
					ptypeAmbient.setDescription(typeAmbient.getDescription());
					ptypeAmbientService.save(ptypeAmbient);
				}
				res.setObj(typeAmbient);
			}
		} catch (Exception e) {
			Sentry.capture(e, "typeAmbient");
			res.setStatus("exception");
			res.setException("Error al crear tipo de ambiente: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/updateTypeAmbient", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateStatus(HttpServletRequest request,
			@Valid @ModelAttribute("TypeAmbient") TypeAmbient typeAmbient, BindingResult errors, ModelMap model,
			Locale locale, HttpSession session) {
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
					TypeAmbient typeStatusOrigin = typeAmbientService.findById(typeAmbient.getId());
					typeStatusOrigin.setName(typeAmbient.getName());
					typeStatusOrigin.setDescription(typeAmbient.getDescription());
					typeAmbientService.update(typeStatusOrigin);
				} else if (profile.equals("postgres")) {
					PTypeAmbient ptypeStatusOrigin = ptypeAmbientService.findById(typeAmbient.getId());
					ptypeStatusOrigin.setName(typeAmbient.getName());
					ptypeStatusOrigin.setDescription(typeAmbient.getDescription());
					ptypeAmbientService.update(ptypeStatusOrigin);
				}

				res.setObj(typeAmbient);
			}
		} catch (Exception e) {
			Sentry.capture(e, "typeAmbient");
			res.setStatus("exception");
			res.setException("Error al modificar tipo de ambiente: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteTypeAmbient/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteStatus(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {

			String profile = profileActive();
			if (profile.equals("oracle")) {
				typeAmbientService.delete(id);
			} else if (profile.equals("postgres")) {
				ptypeAmbientService.delete(id);
			}

			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar tipo de ambiente: " + e.getCause().getCause().getCause().getMessage()
					+ ":" + e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar ambiente: Existen referencias que debe eliminar antes");
			} else {
				Sentry.capture(e, "typeAmbient");
			}

			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}

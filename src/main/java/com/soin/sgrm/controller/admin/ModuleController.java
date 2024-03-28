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
import com.soin.sgrm.model.Module;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.TypeObject;
import com.soin.sgrm.model.pos.PModule;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.model.pos.PTypeObject;
import com.soin.sgrm.service.ModuleService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.pos.PModuleService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/module")
public class ModuleController extends BaseController {

	public static final Logger logger = Logger.getLogger(ModuleController.class);

	@Autowired
	ModuleService moduleService;

	@Autowired
	SystemService systemService;
	
	@Autowired
	PModuleService pmoduleService;

	@Autowired
	PSystemService psystemService;

	private final Environment environment;

	@Autowired
	public ModuleController(Environment environment) {
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
			model.addAttribute("modules", moduleService.list());
			model.addAttribute("module", new Module());
			model.addAttribute("systems", systemService.listAll());
			model.addAttribute("system", new SystemInfo());
		} else if (profile.equals("postgres")) {
			model.addAttribute("modules", pmoduleService.list());
			model.addAttribute("module", new PModule());
			model.addAttribute("systems", psystemService.listAll());
			model.addAttribute("system", new PSystemInfo());
		}
		
		return "/admin/module/module";
	}

	@RequestMapping(value = "/findModule/{id}", method = RequestMethod.GET)
	public @ResponseBody Object findModule(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		try {
			String profile = profileActive();
			if (profile.equals("oracle")) {
				Module module = moduleService.findById(id);
				return module;
			} else if (profile.equals("postgres")) {
				PModule pmodule = pmoduleService.findById(id);
				return pmodule;
			}
			
		} catch (Exception e) {
			Sentry.capture(e, "module");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
		return null;
	}

	@RequestMapping(path = "/saveModule", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveModule(HttpServletRequest request,

			@Valid @ModelAttribute("Module") Module module, BindingResult errors, ModelMap model, Locale locale,
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

			if (module.getSystemId() == null) {
				res.setStatus("fail");
				res.addError("systemId", "Seleccione una opción");
			}

			if (res.getStatus().equals("success")) {
				
				
				String profile = profileActive();
				if (profile.equals("oracle")) {
					module.setSystem(systemService.findById(module.getSystemId()));
					moduleService.save(module);
					res.setObj(module);
				} else if (profile.equals("postgres")) {
					PModule pmodule=new PModule();
					pmodule.setSystem(psystemService.findById(module.getSystemId()));
					pmodule.setDescription(module.getDescription());
					pmodule.setName(module.getName());
					pmoduleService.save(pmodule);
					res.setObj(pmodule);
				}
				
			}
		} catch (Exception e) {
			Sentry.capture(e, "module");
			res.setStatus("exception");
			res.setException("Error al crear modulo: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/updateModule", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateModule(HttpServletRequest request,
			@Valid @ModelAttribute("Module") Module module, BindingResult errors, ModelMap model, Locale locale,
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
			if (module.getSystemId() == null) {
				res.setStatus("fail");
				res.addError("systemId", "Seleccione una opción");
			}

			if (res.getStatus().equals("success")) {
				
				
				String profile = profileActive();
				if (profile.equals("oracle")) {
					Module moduleOrigin = moduleService.findById(module.getId());
					moduleOrigin.setName(module.getName());
					moduleOrigin.setDescription(module.getDescription());
					moduleOrigin.setSystem(systemService.findById(module.getSystemId()));
					moduleService.update(moduleOrigin);
					res.setObj(module);
				} else if (profile.equals("postgres")) {
					PModule pmoduleOrigin = pmoduleService.findById(module.getId());
					pmoduleOrigin.setName(module.getName());
					pmoduleOrigin.setDescription(module.getDescription());
					pmoduleOrigin.setSystem(psystemService.findById(module.getSystemId()));
					pmoduleService.update(pmoduleOrigin);
					res.setObj(pmoduleOrigin);
				}
				
			}
		} catch (Exception e) {
			Sentry.capture(e, "module");
			res.setStatus("exception");
			res.setException("Error al modificar modulo: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteModule/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteModule(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				moduleService.delete(id);
			} else if (profile.equals("postgres")) {
				pmoduleService.delete(id);
			}
		
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar modulo: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar modulo: Existen referencias que debe eliminar antes");
			} else {
				Sentry.capture(e, "module");
			}
		}
		return res;
	}

}

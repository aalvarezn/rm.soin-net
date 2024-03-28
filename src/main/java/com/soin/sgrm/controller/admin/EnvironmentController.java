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
import com.soin.sgrm.model.Environment;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.pos.PEnvironment;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.service.EnvironmentService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.pos.PEnvironmentService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/environment")
public class EnvironmentController extends BaseController {
	
	public static final Logger logger = Logger.getLogger(EnvironmentController.class);

	@Autowired
	EnvironmentService environmentService;

	@Autowired
	SystemService systemService;
	
	@Autowired
	PEnvironmentService penvironmentService;

	@Autowired
	PSystemService psystemService;
	
	private final org.springframework.core.env.Environment env;
	
	
	@Autowired
	public EnvironmentController(org.springframework.core.env.Environment env) {
		this.env = env;
	}

	public String profileActive() {
		String[] activeProfiles = env.getActiveProfiles();
		for (String profile : activeProfiles) {
			return profile;
		}
		return "";
	}

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		
		String profile = profileActive();
		if (profile.equals("oracle")) {
			model.addAttribute("environments", environmentService.list());
			model.addAttribute("environment", new Environment());
			model.addAttribute("systems", systemService.listAll());
			model.addAttribute("system", new SystemInfo());
		} else if (profile.equals("postgres")) {
			model.addAttribute("environments", penvironmentService.list());
			model.addAttribute("environment", new PEnvironment());
			model.addAttribute("systems", psystemService.listAll());
			model.addAttribute("system", new PSystemInfo());
			}
		
		
		return "/admin/environment/environment";
	}

	@RequestMapping(value = "/findEnvironment/{id}", method = RequestMethod.GET)
	public @ResponseBody Object findEnvironment(@PathVariable Integer id, HttpServletRequest request,
			Locale locale, Model model, HttpSession session) {
		try {
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				Environment environment = environmentService.findById(id);
				return environment;
			} else if (profile.equals("postgres")) {
				PEnvironment penvironment = penvironmentService.findById(id);
				return penvironment;
				}
			
		} catch (Exception e) {
			Sentry.capture(e,"environment");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
		return null;
	}

	@RequestMapping(path = "/saveEnvironment", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveEnvironment(HttpServletRequest request,
			@Valid @ModelAttribute("Environment") Environment environment, BindingResult errors, ModelMap model,
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

			if (environment.getSystemId() == null) {
				res.setStatus("fail");
				res.addError("systemId", "Seleccione una opción");
			}

			if (res.getStatus().equals("success")) {
				
				String profile = profileActive();
				if (profile.equals("oracle")) {
					environment.setSystem(systemService.findById(environment.getSystemId()));
					environmentService.save(environment);
					res.setObj(environment);
				} else if (profile.equals("postgres")) {
					PEnvironment penvironment=new PEnvironment();
					penvironment.setDescription(environment.getDescription());
					penvironment.setExternal(environment.getExternal());
					penvironment.setName(environment.getName());
					penvironment.setSystem(psystemService.findById(environment.getSystemId()));
					penvironmentService.save(penvironment);
					res.setObj(penvironment);
					}
				
			}
		} catch (Exception e) {
			Sentry.capture(e,"environment");
			res.setStatus("exception");
			res.setException("Error al crear entorno: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/updateEnvironment", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateEnvironment(HttpServletRequest request,
			@Valid @ModelAttribute("Environment") Environment environment, BindingResult errors, ModelMap model,
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
			if (environment.getSystemId() == null) {
				res.setStatus("fail");
				res.addError("systemId", "Seleccione una opción");
			}

			if (res.getStatus().equals("success")) {
				
				String profile = profileActive();
				if (profile.equals("oracle")) {
					Environment environmentOrigin = environmentService.findById(environment.getId());
					environmentOrigin.setName(environment.getName());
					environmentOrigin.setDescription(environment.getDescription());
					environmentOrigin.setSystem(systemService.findById(environment.getSystemId()));
					environmentOrigin.setExternal(environment.getExternal());
					environmentService.update(environmentOrigin);
					res.setObj(environment);
				} else if (profile.equals("postgres")) {
					PEnvironment penvironmentOrigin = penvironmentService.findById(environment.getId());
					penvironmentOrigin.setName(environment.getName());
					penvironmentOrigin.setDescription(environment.getDescription());
					penvironmentOrigin.setSystem(psystemService.findById(environment.getSystemId()));
					penvironmentOrigin.setExternal(environment.getExternal());
					penvironmentService.update(penvironmentOrigin);
					res.setObj(penvironmentOrigin);
					}
				
			}
		} catch (Exception e) {
			Sentry.capture(e,"environment");
			res.setStatus("exception");
			res.setException("Error al modificar entorno: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteEnvironment/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteEnvironment(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			String profile = profileActive();
			if (profile.equals("oracle")) {
				environmentService.delete(id);
			} else if (profile.equals("postgres")) {
				penvironmentService.delete(id);
				}
			
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar entorno: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar entorno: Existen referencias que debe eliminar antes");
			}else {
				Sentry.capture(e,"environment");
			}
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}

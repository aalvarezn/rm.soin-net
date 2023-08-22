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
import com.soin.sgrm.model.Ambient;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.TypeAmbient;
import com.soin.sgrm.model.pos.PAmbient;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.model.pos.PTypeAmbient;
import com.soin.sgrm.service.AmbientService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.TypeAmbientService;
import com.soin.sgrm.service.pos.PAmbientService;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.service.pos.PTypeAmbientService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/ambient")
public class AmbientController extends BaseController {
	
	public static final Logger logger = Logger.getLogger(AmbientController.class);

	@Autowired
	AmbientService ambientService;

	@Autowired
	SystemService systemService;

	@Autowired
	TypeAmbientService typeAmbientService;
	
	@Autowired
	PAmbientService pambientService;

	@Autowired
	PSystemService psystemService;

	@Autowired
	PTypeAmbientService ptypeAmbientService;


	private final Environment environment;
	

	@Autowired
	public AmbientController(Environment environment) {
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
			model.addAttribute("ambients", ambientService.list());
			model.addAttribute("ambient", new Ambient());
			model.addAttribute("systems", systemService.listAll());
			model.addAttribute("system", new SystemInfo());
			model.addAttribute("typeAmbients", typeAmbientService.list());
			model.addAttribute("typeAmbient", new TypeAmbient());
		} else if (profile.equals("postgres")) {
			model.addAttribute("ambients", pambientService.list());
			model.addAttribute("ambient", new PAmbient());
			model.addAttribute("systems", psystemService.listAll());
			model.addAttribute("system", new PSystemInfo());
			model.addAttribute("typeAmbients", ptypeAmbientService.list());
			model.addAttribute("typeAmbient", new PTypeAmbient());
		}

		return "/admin/ambient/ambient";
	}

	@RequestMapping(value = "/findAmbient/{id}", method = RequestMethod.GET)
	public @ResponseBody Object findAmbient(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		try {
			String profile = profileActive();
			if (profile.equals("oracle")) {
				Ambient ambient = ambientService.findById(id);
				return ambient;
			} else if (profile.equals("postgres")) {
				PAmbient ambient = pambientService.findById(id);
				return ambient;
			}
			return null;
			
		} catch (Exception e) {
			Sentry.capture(e, "ambient");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}

	@RequestMapping(path = "/saveAmbient", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveAmbient(HttpServletRequest request,

			@Valid @ModelAttribute("Ambient") Ambient ambient, BindingResult errors, ModelMap model, Locale locale,
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
			
			if (ambient.getSystemId() == null) {
				res.setStatus("fail");
				res.addError("systemId", "Seleccione una opción");
			}
			
			if (ambient.getTypeAmbientId() == null) {
				res.setStatus("fail");
				res.addError("typeAmbientId", "Seleccione una opción");
			}
			
			if (res.getStatus().equals("success")) {
				
				String profile = profileActive();
				if (profile.equals("oracle")) {
					ambient.setSystem(systemService.findSystemUserById(ambient.getSystemId()));
					ambient.setTypeAmbient(typeAmbientService.findById(ambient.getTypeAmbientId()));
					ambientService.save(ambient);
					res.setObj(ambient);
				} else if (profile.equals("postgres")) {
					PAmbient pambient=new PAmbient();
					pambient.setName(ambient.getName());
					pambient.setDetails(ambient.getDetails());
					pambient.setCode(ambient.getCode());
					pambient.setServerName(ambient.getServerName());
					pambient.setSystem(psystemService.findSystemUserById(ambient.getSystemId()));
					pambient.setTypeAmbient(ptypeAmbientService.findById(ambient.getTypeAmbientId()));
					pambientService.save(pambient);
					res.setObj(pambient);
				}
				
			}
		} catch (Exception e) {
			Sentry.capture(e, "ambient");
			res.setStatus("exception");
			res.setException("Error al crear ambiente: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/updateAmbient", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateAmbient(HttpServletRequest request,
			@Valid @ModelAttribute("Ambient") Ambient ambient, BindingResult errors, ModelMap model, Locale locale,
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
			if (ambient.getSystemId() == null) {
				res.setStatus("fail");
				res.addError("systemId", "Seleccione una opción");
			}
			
			if (ambient.getTypeAmbientId() == null) {
				res.setStatus("fail");
				res.addError("typeAmbientId", "Seleccione una opción");
			}
			
			if (res.getStatus().equals("success")) {
				
				String profile = profileActive();
				if (profile.equals("oracle")) {
					Ambient ambientOrigin = ambientService.findById(ambient.getId());
					ambientOrigin.setSystem(systemService.findSystemUserById(ambient.getSystemId()));
					ambientOrigin.setTypeAmbient(typeAmbientService.findById(ambient.getTypeAmbientId()));
					ambientOrigin.setCode(ambient.getCode());
					ambientOrigin.setName(ambient.getName());
					ambientOrigin.setDetails(ambient.getDetails());
					ambientOrigin.setServerName(ambient.getServerName());
					ambientService.update(ambientOrigin);
					res.setObj(ambient);
				} else if (profile.equals("postgres")) {
					PAmbient pambientOrigin = pambientService.findById(ambient.getId());
					pambientOrigin.setSystem(psystemService.findSystemUserById(ambient.getSystemId()));
					pambientOrigin.setTypeAmbient(ptypeAmbientService.findById(ambient.getTypeAmbientId()));
					pambientOrigin.setCode(ambient.getCode());
					pambientOrigin.setName(ambient.getName());
					pambientOrigin.setDetails(ambient.getDetails());
					pambientOrigin.setServerName(ambient.getServerName());
					pambientService.update(pambientOrigin);
					res.setObj(ambient);
				}

			}
		} catch (Exception e) {
			Sentry.capture(e, "ambient");
			res.setStatus("exception");
			res.setException("Error al modificar ambiente: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteAmbient/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteAmbient(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			String profile = profileActive();
			if (profile.equals("oracle")) {
				ambientService.delete(id);
			} else if (profile.equals("postgres")) {
				pambientService.delete(id);
			}
			
			
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar ambiente: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());
			
			if(e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar ambiente: Existen referencias que debe eliminar antes");
			}else {
				Sentry.capture(e, "ambient");
			}
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}

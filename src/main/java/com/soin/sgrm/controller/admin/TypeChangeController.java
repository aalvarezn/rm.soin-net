package com.soin.sgrm.controller.admin;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.TypeAmbient;
import com.soin.sgrm.model.TypeChange;
import com.soin.sgrm.model.pos.PImpact;
import com.soin.sgrm.model.pos.PTypeChange;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.TypeChangeService;
import com.soin.sgrm.service.pos.PTypeChangeService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/typeChange")
public class TypeChangeController extends BaseController {
	public static final Logger logger = Logger.getLogger(TypeChangeController.class);

	@Autowired
	TypeChangeService typeChangeService;
	
	@Autowired
	PTypeChangeService ptypeChangeService;
	
	private final Environment environment;
	
	@Autowired
	public TypeChangeController(Environment environment) {
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

		return "/admin/typeChange/typeChange";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		
		try {
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				JsonSheet<TypeChange> typeChange = new JsonSheet<>();
				typeChange.setData(typeChangeService.findAll());
				return typeChange;
			} else if (profile.equals("postgres")) {
				JsonSheet<PTypeChange> typeChange = new JsonSheet<>();
				typeChange.setData(ptypeChangeService.findAll());
				return typeChange;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody TypeChange addTypeChange) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			String profile = profileActive();
			if (profile.equals("oracle")) {
				typeChangeService.save(addTypeChange);
			} else if (profile.equals("postgres")) {
				PTypeChange paddTypeChange=new PTypeChange();
				paddTypeChange.setName(addTypeChange.getName());
				paddTypeChange.setDescription(addTypeChange.getDescription());
				ptypeChangeService.save(paddTypeChange);
			}
			

			res.setMessage("Tipo cambio agregado!");
		} catch (Exception e) {
			Sentry.capture(e, "typeChange");
			res.setStatus("exception");
			res.setMessage("Error al agregar Tipo cambio!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody TypeChange uptTypeChange) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				typeChangeService.update(uptTypeChange);
			} else if (profile.equals("postgres")) {
				PTypeChange puptTypeChange=new PTypeChange();
				puptTypeChange.setId(uptTypeChange.getId());
				puptTypeChange.setDescription(uptTypeChange.getDescription());
				puptTypeChange.setName(uptTypeChange.getName());
				ptypeChangeService.update(puptTypeChange);
			}
			

			res.setMessage("Tipo cambio modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "typeChange");
			res.setStatus("exception");
			res.setMessage("Error al modificar Tipo cambio!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			String profile = profileActive();
			if (profile.equals("oracle")) {
				typeChangeService.delete(id);
			} else if (profile.equals("postgres")) {
				ptypeChangeService.delete(id);
			}
			
			res.setMessage("Tipo cambio  eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "typeChange");
			res.setStatus("exception");
			res.setMessage("Error al eliminar el Tipo cambio!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}

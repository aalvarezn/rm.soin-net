package com.soin.sgrm.controller.wf;

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
import com.soin.sgrm.controller.admin.ConfigurationItemController;
import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.pos.PIncidence;
import com.soin.sgrm.model.pos.wf.PType;
import com.soin.sgrm.model.wf.Type;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.pos.wf.PTypeService;
import com.soin.sgrm.service.wf.TypeService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/wf/type")
public class TypeController extends BaseController {

	public static final Logger logger = Logger.getLogger(TypeController.class);

	@Autowired
	TypeService typeService;
	
	@Autowired
	PTypeService ptypeService;

	private final Environment environment;
	

	@Autowired
	public TypeController(Environment environment) {
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
		if (profileActive().equals("oracle")) {
			model.addAttribute("types", typeService.list());
			model.addAttribute("type", new Type());
		} else if (profileActive().equals("postgres")) {
			model.addAttribute("types", ptypeService.list());
			model.addAttribute("type", new Type());
		}
		
		return "/wf/type/type";
	}

	@RequestMapping(value = "/findType/{id}", method = RequestMethod.GET)
	public @ResponseBody Object findType(@PathVariable Integer id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
		try {
			if (profileActive().equals("oracle")) {
				Type type = typeService.findById(id);
				return type;
			} else if (profileActive().equals("postgres")) {
				PType type = ptypeService.findById(id);
				return type;
			}
			
			
		} catch (Exception e) {
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
		return null;
	}

	@RequestMapping(path = "/saveType", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveType(HttpServletRequest request,
			@Valid @ModelAttribute("Type") Type type, BindingResult errors, ModelMap model, Locale locale,
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
				if (profileActive().equals("oracle")) {
					typeService.save(type);
					res.setObj(type);
				} else if (profileActive().equals("postgres")) {
					PType ptype=new PType();
					ptype.setName(type.getName());
					ptypeService.save(ptype);
					res.setObj(type);
				}
				
			
			}
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al crear tipo: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			e.printStackTrace();
		}
		return res;
	}

	@RequestMapping(value = "/updateType", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateType(HttpServletRequest request, @Valid @ModelAttribute("Type") Type type,
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
				if (profileActive().equals("oracle")) {
					Type typeOrigin = typeService.findById(type.getId());
					typeOrigin.setName(type.getName());
					typeService.update(typeOrigin);
					res.setObj(type);
				} else if (profileActive().equals("postgres")) {
					PType typeOrigin = ptypeService.findById(type.getId());
					typeOrigin.setName(type.getName());
					ptypeService.update(typeOrigin);
					res.setObj(type);
				}
				
				
			}
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al modificar tipo: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteType/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteType(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			if (profileActive().equals("oracle")) {
				typeService.delete(id);
			} else if (profileActive().equals("postgres")) {
				ptypeService.delete(id);
			}
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar tipo: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar tipo: Existen referencias que debe eliminar antes");
			}

			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			e.printStackTrace();
		}
		return res;
	}

}

package com.soin.sgrm.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.PriorityIncidence;
import com.soin.sgrm.model.EmailIncidence;
import com.soin.sgrm.model.System_Priority;
import com.soin.sgrm.model.TypeIncidence;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.PriorityIncidenceService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.EmailIncidenceService;
import com.soin.sgrm.service.System_PriorityService;
import com.soin.sgrm.service.TypeIncidenceService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/emailIn")
public class EmailIncidenceController extends BaseController {
	public static final Logger logger = Logger.getLogger(EmailIncidenceController.class);

	@Autowired
	EmailIncidenceService emailIncidenceService;

	@Autowired
	SystemService systemService;

	@Autowired
	TypeIncidenceService typeIncidenceService;
	
	@Autowired
	EmailTemplateService emailTemplateService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		Integer idUser = getUserLogin().getId();
		List<com.soin.sgrm.model.System> systems = systemService.findByManagerIncidence(idUser);
		List<EmailTemplate> emailTemplates =emailTemplateService.listAll();
		model.addAttribute("emailTemplates", emailTemplates);
		model.addAttribute("systems", systems);
		return "/EmailIncidence/EmailIncidence";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<EmailIncidence> systemType = new JsonSheet<>();
		try {
			systemType.setData(emailIncidenceService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return systemType;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody EmailIncidence addSystemType) {
		JsonResponse res = new JsonResponse();
		try {
			
			
		
		} catch (Exception e) {
			Sentry.capture(e, "systemType");
			res.setStatus("error");
			res.setMessage("Error al agregar el tipo al sistema!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request,
			@RequestBody EmailIncidence uptEmailIncidence) {
		JsonResponse res = new JsonResponse();
	
			return	res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			emailIncidenceService.delete(id);
			res.setStatus("success");
			res.setMessage("Se elimino el tipo correctamente!");
		} catch (Exception e) {
			Sentry.capture(e, "EmailIncidence");
			res.setStatus("error");
			res.setMessage("Error al eliminar el tipo!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = { "/changeTypeIncidence/{id}" }, method = RequestMethod.GET)
	public @ResponseBody List<TypeIncidence> changePriority(@PathVariable Integer id, Locale locale, Model model) {
		List<TypeIncidence> listTypeIncidence = new ArrayList<TypeIncidence>();
		List<TypeIncidence> listAllTypeIncidence = typeIncidenceService.findAll();
		List<EmailIncidence> listSystemPriority = emailIncidenceService.findBySystem(id);

		try {
			for (TypeIncidence typeIncidence : listAllTypeIncidence) {
				Boolean veri = false;
				if(listSystemPriority.size()>0) {
				for (EmailIncidence systemPriority : listSystemPriority) {
					
					
				}
				}else {
					veri=false;
				}
				if (!veri) {
					listTypeIncidence.add(typeIncidence);
				}
			}
		} catch (Exception e) {
			Sentry.capture(e, "systemIncidence");

			e.printStackTrace();
		}

		return listTypeIncidence;
	}
	
	@RequestMapping(value = { "/getypeIncidence/{id}" }, method = RequestMethod.GET)
	public @ResponseBody List<EmailIncidence> getType(@PathVariable Integer id, Locale locale, Model model) {
	
		List<EmailIncidence> listSystemType = null;

		try {
			 listSystemType = emailIncidenceService.findBySystem(id);
			
		} catch (Exception e) {
			Sentry.capture(e, "systemIncidence");

			e.printStackTrace();
		}

		return listSystemType;
	}
}

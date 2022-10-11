package com.soin.sgrm.controller.admin;

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
import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.TypePetitionService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/typePetition")
public class TypePetitionController extends BaseController {
	public static final Logger logger = Logger.getLogger(TypePetitionController.class);

	@Autowired
	TypePetitionService typePetitionService;
	
	@Autowired
	EmailTemplateService emailTemplateService;
	
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("emailTemplates",emailTemplateService.listAll());
		model.addAttribute("emailTemplate",new EmailTemplate());
		return "/admin/typePetition/typePetition";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<TypePetition> typePetition = new JsonSheet<>();
		try {
			typePetition.setData(typePetitionService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return typePetition;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody TypePetition addTypePetition) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			addTypePetition.setStatus(1);
			EmailTemplate email=emailTemplateService.findById(addTypePetition.getEmailTemplateId());
			addTypePetition.setEmailTemplate(email);
			typePetitionService.save(addTypePetition);

			res.setMessage("Tipo solicitud agregada!");
		} catch (Exception e) {
			Sentry.capture(e, "typePetition");
			res.setStatus("exception");
			res.setMessage("Error al agregar Tipo solicitud!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody TypePetition uptTypePetition) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			EmailTemplate email=emailTemplateService.findById(uptTypePetition.getEmailTemplateId());
			uptTypePetition.setEmailTemplate(email);
			TypePetition petitionOld=typePetitionService.findById(uptTypePetition.getId());
			uptTypePetition.setStatus(petitionOld.getStatus());
			typePetitionService.update(uptTypePetition);
			
			res.setMessage("Tipo solicitud modificada!");
		} catch (Exception e) {
			Sentry.capture(e, "typeChange");
			res.setStatus("exception");
			res.setMessage("Error al modificar Tipo solicitud!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			TypePetition typeUpt=typePetitionService.findById(id);
			if(typeUpt.getStatus()==1) {
				typeUpt.setStatus(0);
				res.setMessage("Tipo solicitud desactivado!");
			}else {
				typeUpt.setStatus(1);
				res.setMessage("Tipo solicitud activada!");
			}
			res.setStatus("success");
			typePetitionService.update(typeUpt);
			
		} catch (Exception e) {
			Sentry.capture(e, "typePetition");
			res.setStatus("exception");
			res.setMessage("Error al modificaar el Tipo solicitud!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}

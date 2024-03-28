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
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.TypeAmbient;
import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.model.pos.PEmailTemplate;
import com.soin.sgrm.model.pos.PImpact;
import com.soin.sgrm.model.pos.PTypePetition;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.TypePetitionService;
import com.soin.sgrm.service.pos.PEmailTemplateService;
import com.soin.sgrm.service.pos.PTypePetitionService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/typePetition")
public class TypePetitionController extends BaseController {
	public static final Logger logger = Logger.getLogger(TypePetitionController.class);

	@Autowired
	TypePetitionService typePetitionService;
	
	@Autowired
	PTypePetitionService ptypePetitionService;
	
	@Autowired
	EmailTemplateService emailTemplateService;
	
	@Autowired
	PEmailTemplateService pemailTemplateService;
	private final Environment environment;

	@Autowired
	public TypePetitionController(Environment environment) {
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
			model.addAttribute("emailTemplates",emailTemplateService.listAll());
			model.addAttribute("emailTemplate",new EmailTemplate());
		} else if (profile.equals("postgres")) {
			model.addAttribute("emailTemplates",pemailTemplateService.listAll());
			model.addAttribute("emailTemplate",new EmailTemplate());
		}
	
		return "/admin/typePetition/typePetition";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		
		try {
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				JsonSheet<TypePetition> typePetition = new JsonSheet<>();
				typePetition.setData(typePetitionService.findAll());
				return typePetition;
			} else if (profile.equals("postgres")) {
				JsonSheet<PTypePetition> typePetition = new JsonSheet<>();
				typePetition.setData(ptypePetitionService.findAll());
				return typePetition;
			}
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody TypePetition addTypePetition) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			String profile = profileActive();
			if (profile.equals("oracle")) {
				addTypePetition.setStatus(1);
				EmailTemplate email=emailTemplateService.findById(addTypePetition.getEmailTemplateId());
				addTypePetition.setEmailTemplate(email);
				typePetitionService.save(addTypePetition);
				
			} else if (profile.equals("postgres")) {
				PTypePetition paddTypePetition=new PTypePetition();
				paddTypePetition.setCode(addTypePetition.getCode());
				paddTypePetition.setDescription(addTypePetition.getDescription());
				paddTypePetition.setStatus(1);
				PEmailTemplate pemail=pemailTemplateService.findById(addTypePetition.getEmailTemplateId());
				paddTypePetition.setEmailTemplate(pemail);
				ptypePetitionService.save(paddTypePetition);
			}
			

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
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				EmailTemplate email=emailTemplateService.findById(uptTypePetition.getEmailTemplateId());
				uptTypePetition.setEmailTemplate(email);
				TypePetition petitionOld=typePetitionService.findById(uptTypePetition.getId());
				uptTypePetition.setStatus(petitionOld.getStatus());
				typePetitionService.update(uptTypePetition);
				
			} else if (profile.equals("postgres")) {
				PTypePetition puptTypePetition=new PTypePetition();
				PEmailTemplate email=pemailTemplateService.findById(uptTypePetition.getEmailTemplateId());
				puptTypePetition.setEmailTemplate(email);
				puptTypePetition.setId(uptTypePetition.getId());
				puptTypePetition.setCode(uptTypePetition.getCode());
				puptTypePetition.setDescription(uptTypePetition.getDescription());
				PTypePetition ppetitionOld=ptypePetitionService.findById(uptTypePetition.getId());
				puptTypePetition.setStatus(ppetitionOld.getStatus());
				ptypePetitionService.update(puptTypePetition);
			}
			
			
			
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
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
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
				
			} else if (profile.equals("postgres")) {
				PTypePetition typeUpt=ptypePetitionService.findById(id);
				if(typeUpt.getStatus()==1) {
					typeUpt.setStatus(0);
					res.setMessage("Tipo solicitud desactivado!");
				}else {
					typeUpt.setStatus(1);
					res.setMessage("Tipo solicitud activada!");
				}
				res.setStatus("success");
				ptypePetitionService.update(typeUpt);
			}
			
			
			
		} catch (Exception e) {
			Sentry.capture(e, "typePetition");
			res.setStatus("exception");
			res.setMessage("Error al modificaar el Tipo solicitud!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}

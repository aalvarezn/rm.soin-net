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
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.TypeIncidence;
import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.TypeIncidenceService;
import com.soin.sgrm.service.TypePetitionService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/typeIncidence")
public class TypeIncidenceController extends BaseController {
	public static final Logger logger = Logger.getLogger(TypeIncidenceController.class);

	@Autowired
	TypeIncidenceService typeIncidenceService;
	
	@Autowired
	EmailTemplateService emailTemplateService;
	
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("emailTemplates",emailTemplateService.listAll());
		model.addAttribute("emailTemplate",new EmailTemplate());
		return "/admin/typeIncidence/typeIncidence";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<TypeIncidence> typeIncidence = new JsonSheet<>();
		try {
			typeIncidence.setData(typeIncidenceService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return typeIncidence;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody TypeIncidence addTypeIncidence) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			addTypeIncidence.setStatus(1);
			EmailTemplate email=emailTemplateService.findById(addTypeIncidence.getEmailTemplateId());
			addTypeIncidence.setEmailTemplate(email);
			TypeIncidence codeIncidence= typeIncidenceService.findByKey("code", addTypeIncidence.getCode());
			if(codeIncidence==null) {
				typeIncidenceService.save(addTypeIncidence);
				res.setMessage("Tipo incidencia agregada!");
			}else {
				res.setStatus("error");
				res.setMessage("Error al agregar incidencia codigo ya utilizado!");
			}
			

			
		} catch (Exception e) {
			Sentry.capture(e, "typeIncidence");
			res.setStatus("error");
			res.setMessage("Error al agregar Tipo incidencia!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody TypeIncidence uptTypeIncidence) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			EmailTemplate email=emailTemplateService.findById(uptTypeIncidence.getEmailTemplateId());
			uptTypeIncidence.setEmailTemplate(email);
			TypeIncidence petitionOld=typeIncidenceService.findById(uptTypeIncidence.getId());
			uptTypeIncidence.setStatus(petitionOld.getStatus());
			
			if(petitionOld.getCode()!=uptTypeIncidence.getCode()){
				
				TypeIncidence incidenceVerification=typeIncidenceService.findByKey("code", uptTypeIncidence.getCode());
				if(incidenceVerification==null) {
					typeIncidenceService.update(uptTypeIncidence);
					res.setMessage("Siges modificado!");
				}else {
					if(incidenceVerification.getId()==uptTypeIncidence.getId()) {
						typeIncidenceService.update(uptTypeIncidence);
						res.setMessage("Siges modificado!");
					}else {
						res.setStatus("error");
						res.setMessage("Error al modificar tipo de incidencia este codigo ya pertenece a otro!");
					}
				}
			}else {
				typeIncidenceService.update(uptTypeIncidence);
				res.setMessage("Siges modificado!");
			}
			res.setMessage("Tipo solicitud modificada!");
		} catch (Exception e) {
			Sentry.capture(e, "typeChange");
			res.setStatus("error");
			res.setMessage("Error al modificar Tipo solicitud!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			TypeIncidence typeUpt=typeIncidenceService.findById(id);
			if(typeUpt.getStatus()==1) {
				typeUpt.setStatus(0);
				res.setMessage("Tipo incidencia desactivado!");
			}else {
				typeUpt.setStatus(1);
				res.setMessage("Tipo incidencia activada!");
			}
			res.setStatus("success");
			typeIncidenceService.update(typeUpt);
			
		} catch (Exception e) {
			Sentry.capture(e, "typeIncidence");
			res.setStatus("error");
			res.setMessage("Error al modificar el Tipo incidencia!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}

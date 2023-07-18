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
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.service.SigesService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/siges")
public class SigesController extends BaseController {
	public static final Logger logger = Logger.getLogger(AmbientController.class);

	@Autowired
	SigesService sigesService;
	
	@Autowired
	SystemService systemService;
	
	@Autowired 
	EmailTemplateService emailTemplateService;
	
	@Autowired 
	ProjectService proyectService;
	
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("systems",systemService.listAll());
		model.addAttribute("system",new System());
		model.addAttribute("emailTemplates",emailTemplateService.listAll());
		model.addAttribute("emailTemplate",new EmailTemplate());
		return "/admin/siges/siges";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<Siges> rfcs = new JsonSheet<>();
		try {
			rfcs.setData(sigesService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rfcs;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody Siges addSiges) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			SystemInfo system= systemService.findSystemInfoById(addSiges.getSystemId());
			EmailTemplate emailTemplate= emailTemplateService.findById(addSiges.getEmailTemplateId());
			addSiges.setEmailTemplate(emailTemplate);
			addSiges.setSystem(system);
			Project proyect =proyectService.findById(system.getProyectId());
			
			if(proyect.getAllowRepeat()) {
				
			}
			
			if (!proyect.getAllowRepeat() && !sigesService.checkUniqueCode(addSiges.getCodeSiges())) {
				res.setStatus("error");
				res.setMessage(
						"Error al crear sistema,codigo proyecto ya utilizado para un mismo proyecto,este proyecto no permite codigo repetido!");
			}else {
				sigesService.save(addSiges);
				res.setMessage("Siges agregado!");
			}
			
		
			
		} catch (Exception e) {
			Sentry.capture(e, "siges");
			res.setStatus("error");
			res.setMessage("Error al agregar siges!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody Siges uptSiges) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			SystemInfo system= systemService.findSystemInfoById(uptSiges.getSystemId());
			EmailTemplate emailTemplate= emailTemplateService.findById( uptSiges.getEmailTemplateId());
			uptSiges.setEmailTemplate(emailTemplate);
			uptSiges.setSystem(system);
			Project proyect =proyectService.findById(system.getProyectId());
			Siges sigesCode=sigesService.findById(uptSiges.getId());
			if(sigesCode.getCodeSiges()!=uptSiges.getCodeSiges()){
				
				Siges sigesVerification=sigesService.findByKey("codeSiges", uptSiges.getCodeSiges());
				if(sigesVerification.getId()==uptSiges.getId()) {
					sigesService.update(uptSiges);
					res.setMessage("Siges modificado!");
				}else {
					
					if(proyect.getAllowRepeat()) {
						sigesService.update(uptSiges);
						res.setMessage("Siges modificado!");
					}else {
						res.setStatus("error");
						res.setMessage("Error al modificar siges este codigo ya pertenece a otro!");
					}
				
				}
			}else {
				sigesService.update(uptSiges);
				res.setMessage("Siges modificado!");
			}
			

		} catch (Exception e) {
			Sentry.capture(e, "siges");
			res.setStatus("error");
			res.setMessage("Error al modificar siges!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			sigesService.delete(id);
			res.setMessage("Siges eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "siges");
			res.setStatus("error");
			res.setMessage("Error al eliminar el siges!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}

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
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.Siges;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.pos.PEmailTemplate;
import com.soin.sgrm.model.pos.PProject;
import com.soin.sgrm.model.pos.PSiges;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.ProjectService;
import com.soin.sgrm.service.SigesService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.pos.PEmailTemplateService;
import com.soin.sgrm.service.pos.PSigesService;
import com.soin.sgrm.service.pos.PSystemService;
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
  
	@Autowired
	PSigesService psigesService;
	
	@Autowired
	PSystemService psystemService;
	
	@Autowired 
	PEmailTemplateService pemailTemplateService;
	
	private final Environment environment;
	
	
	@Autowired
	public SigesController(Environment environment) {
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
			model.addAttribute("systems",systemService.listAll());
			model.addAttribute("system",new Project());
			model.addAttribute("emailTemplates",emailTemplateService.listAll());
			model.addAttribute("emailTemplate",new EmailTemplate());
		} else if (profile.equals("postgres")) {
			model.addAttribute("systems",psystemService.listAll());
			model.addAttribute("system",new PProject());
			model.addAttribute("emailTemplates",pemailTemplateService.listAll());
			model.addAttribute("emailTemplate",new PEmailTemplate());
		}
		
		
		return "/admin/siges/siges";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		
		try {
			String profile = profileActive();
			if (profile.equals("oracle")) {
				JsonSheet<Siges> rfcs = new JsonSheet<>();
				
				rfcs.setData(sigesService.findAll());
				return rfcs;
			} else if (profile.equals("postgres")) {
				JsonSheet<PSiges> rfcs = new JsonSheet<>();
				rfcs.setData(psigesService.findAll());
				return rfcs;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
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
			
		
			String profile = profileActive();
			if (profile.equals("oracle")) {
				SystemInfo system= systemService.findSystemInfoById(addSiges.getSystemId());
				EmailTemplate emailTemplate= emailTemplateService.findById(addSiges.getEmailTemplateId());
				addSiges.setEmailTemplate(emailTemplate);
				addSiges.setSystem(system);
				Siges codeSiges= sigesService.findByKey("codeSiges", addSiges.getCodeSiges().trim());
				if(codeSiges==null) {
				sigesService.save(addSiges);
				res.setMessage("Siges agregado!");
				}else {
					res.setStatus("exception");
					res.setMessage("Error al agregar siges codigo Siges ya utilizado!");
				}
			} else if (profile.equals("postgres")) {
				PSiges paddSiges=new PSiges();
				PSystemInfo psystem= psystemService.findSystemInfoById(addSiges.getSystemId());
				PEmailTemplate pemailTemplate= pemailTemplateService.findById(addSiges.getEmailTemplateId());
				paddSiges.setEmailTemplate(pemailTemplate);
				paddSiges.setSystem(psystem);
				paddSiges.setCodeSiges(addSiges.getCodeSiges());
				PSiges pcodeSiges= psigesService.findByKey("codeSiges", addSiges.getCodeSiges().trim());
				if(pcodeSiges==null) {
				psigesService.save(paddSiges);
				res.setMessage("Siges agregado!");
				}else {
					res.setStatus("exception");
					res.setMessage("Error al agregar siges codigo Siges ya utilizado!");
				}
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
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				SystemInfo system= systemService.findSystemInfoById(uptSiges.getSystemId());
				EmailTemplate emailTemplate= emailTemplateService.findById( uptSiges.getEmailTemplateId());
				uptSiges.setEmailTemplate(emailTemplate);
				uptSiges.setSystem(system);
				Siges sigesCode=sigesService.findById(uptSiges.getId());
				if(sigesCode.getCodeSiges()!=uptSiges.getCodeSiges()){
					
					Siges sigesVerification=sigesService.findByKey("codeSiges", uptSiges.getCodeSiges());
					if(sigesVerification==null) {
						sigesService.update(uptSiges);
						res.setMessage("Siges modificado!");
					}else {
						if(sigesVerification.getId()==uptSiges.getId()) {
							sigesService.update(uptSiges);
							res.setMessage("Siges modificado!");
						}else {
							res.setStatus("exception");
							res.setMessage("Error al modificar siges este codigo ya pertenece a otro!");
						}
					}
				
				}else {
					sigesService.update(uptSiges);
					res.setMessage("Siges modificado!");
				}
			} else if (profile.equals("postgres")) {
				PSiges puptSiges= new PSiges();
				PSystemInfo system= psystemService.findSystemInfoById(uptSiges.getSystemId());
				PEmailTemplate pemailTemplate= pemailTemplateService.findById( uptSiges.getEmailTemplateId());
				puptSiges.setEmailTemplate(pemailTemplate);
				puptSiges.setSystem(system);
				puptSiges.setId(uptSiges.getId());
				puptSiges.setCodeSiges(uptSiges.getCodeSiges());
				PSiges sigesCode=psigesService.findById(uptSiges.getId());
				if(sigesCode.getCodeSiges()!=uptSiges.getCodeSiges()){
					
					PSiges psigesVerification=psigesService.findByKey("codeSiges", uptSiges.getCodeSiges());
					
					if(psigesVerification==null) {
						psigesService.update(puptSiges);
						res.setMessage("Siges modificado!");
					}else {
						if(psigesVerification.getId()==puptSiges.getId()) {
							psigesService.update(puptSiges);
							res.setMessage("Siges modificado!");
						}else {
							res.setStatus("exception");
							res.setMessage("Error al modificar siges este codigo ya pertenece a otro!");
						}
					}
					
				}else {
					psigesService.update(puptSiges);
					res.setMessage("Siges modificado!");
				}
			}
			
			
			

		} catch (Exception e) {
			Sentry.capture(e, "siges");
			res.setStatus("exception");
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
			String profile = profileActive();
			if (profile.equals("oracle")) {
				sigesService.delete(id);
			} else if (profile.equals("postgres")) {
				psigesService.delete(id);
			}
			
			res.setMessage("Siges eliminado!");
		} catch (Exception e) {
			Sentry.capture(e, "siges");
			res.setStatus("exception");
			res.setMessage("Error al eliminar el siges!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}

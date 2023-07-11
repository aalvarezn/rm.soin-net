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
import com.soin.sgrm.model.EmailTemplate;
import com.soin.sgrm.model.TypeAmbient;
import com.soin.sgrm.model.pos.PEmailTemplate;
import com.soin.sgrm.model.pos.PImpact;
import com.soin.sgrm.security.UserLogin;
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.service.pos.PEmailTemplateService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/email")
public class EmailController extends BaseController {

	public static final Logger logger = Logger.getLogger(EmailController.class);

	@Autowired
	EmailTemplateService emailService;
	
	@Autowired
	PEmailTemplateService pemailService;

	private final Environment environment;

	@Autowired
	public EmailController(Environment environment) {
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
			model.addAttribute("emails", emailService.listAll());
		} else if (profile.equals("postgres")) {
			model.addAttribute("emails", pemailService.listAll());
		}
		
		return "/admin/email/email";
	}

	@RequestMapping(value = { "/editarEmail-{id}" }, method = RequestMethod.GET)
	public String editEmail(@PathVariable Integer id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
		
		String profile = profileActive();
		if (profile.equals("oracle")) {
			model.addAttribute("email", emailService.findById(id));
		} else if (profile.equals("postgres")) {
			model.addAttribute("email", pemailService.findById(id));
		}
		
		
		return "/admin/email/editemail";
	}

	@RequestMapping(path = "/emailTest", method = RequestMethod.POST)
	public @ResponseBody JsonResponse emailTest(HttpServletRequest request,
			@Valid @ModelAttribute("EmailTemplate") EmailTemplate email, BindingResult errors, ModelMap model,
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
			if (email.getTo() == null || ((email.getTo() != null) ? email.getTo() : "").trim().equals("")) {
				res.setStatus("fail");
				res.addError("to", Constant.EMPTY);
			}

			String cc_invalid = "";
			String[] ccAddress = email.getCc().split(",");
			for (int i = 0; i < ccAddress.length; i++) {
				if (!CommonUtils.isValidEmailAddress(ccAddress[i])) {
					cc_invalid += ( cc_invalid.equals("")) ? (ccAddress[i]) : (","+ccAddress[i] );
				}
			}
			if (!cc_invalid.equals("")) {
				res.addError("cc", "dirección(es) inválida(s) " + cc_invalid);
				res.setStatus("fail");
			}

			String to_invalid = "";
			String[] toAddress = email.getTo().split(",");
			for (int i = 0; i < toAddress.length; i++) {
				if (!CommonUtils.isValidEmailAddress(toAddress[i])) {
					to_invalid += (to_invalid.equals("")) ? (toAddress[i]) : (","+toAddress[i] );
				}
			}
			if (!to_invalid.equals("")) {
				res.addError("to", "dirección(es) inválida(s) " + to_invalid);
				res.setStatus("fail");
			}

			if (res.getStatus().equals("fail")) {
				return res;
			}
			String profile = profileActive();
			if (profile.equals("oracle")) {
				emailService.sendMail(email.getTo(), email.getCc(), email.getSubject(), email.getHtml());
			} else if (profile.equals("postgres")) {
				pemailService.sendMail(email.getTo(), email.getCc(), email.getSubject(), email.getHtml());
			}
			

		} catch (Exception e) {
			Sentry.capture(e, "email");
			res.setStatus("exception");
			res.setException("Error al enviar correo: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;

	}

	@RequestMapping(path = "/saveEmail", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveEmail(HttpServletRequest request,
			@Valid @ModelAttribute("EmailTemplate") EmailTemplate email, BindingResult errors, ModelMap model,
			Locale locale, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			String profile = profileActive();
			res.setStatus("success");
			if (email.getName() == null || ((email.getName() != null) ? email.getName() : "").trim().equals("")) {
				res.setStatus("fail");
				res.addError("name", Constant.EMPTY);
			}
			if (email.getSubject() == null
					|| ((email.getSubject() != null) ? email.getSubject() : "").trim().equals("")) {
				res.setStatus("fail");
				res.addError("subject", Constant.EMPTY);
			}
			if (email.getName() != null) {
				if (profile.equals("oracle")) {
					if (emailService.existEmailTemplate(email.getName().trim())) {
						res.setStatus("fail");
						res.addError("name", "Nombre de plantilla ya existe");
					}
				} else if (profile.equals("postgres")) {
					if (pemailService.existEmailTemplate(email.getName().trim())) {
						res.setStatus("fail");
						res.addError("name", "Nombre de plantilla ya existe");
					}
				}
				
			}
			if (res.getStatus().equals("fail")) {
				return res;
			}
			email.setRetry(0);
			email.setCreatedormodify(CommonUtils.getSystemTimestamp());
			email.setState(0);
			email.setUsermodify(getUserLogin().getId());
			if (profile.equals("oracle")) {
				
				emailService.saveEmail(email);
				res.setObj(email);
			} else if (profile.equals("postgres")) {
				PEmailTemplate pEmail=new PEmailTemplate();
				pEmail.setCc(email.getCc());
				pEmail.setName(email.getName());
				pEmail.setSubject(email.getSubject());
				pEmail.setCreatedormodify(CommonUtils.getSystemTimestamp());
				pEmail.setState(0);
				pEmail.setRetry(0);
				pEmail.setUsermodify(getUserLogin().getId());
				pemailService.saveEmail(pEmail);
				res.setObj(pEmail);
			}
			
		
			
		} catch (Exception e) {
			Sentry.capture(e, "email");
			res.setStatus("exception");
			res.setException("Error al crear correo: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(path = "/updateEmail", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateEmail(HttpServletRequest request,
			@Valid @ModelAttribute("EmailTemplate") EmailTemplate email, BindingResult errors, ModelMap model,
			Locale locale, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			String profile = profileActive();
			res.setStatus("success");
			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
			}

			if (email.getName() != null) {
				
				if (profile.equals("oracle")) {
					if (emailService.existEmailTemplate(email.getName().trim(), email.getId())) {
						res.setStatus("fail");
						res.addError("name", "Nombre de plantilla ya existe");
					}
				} else if (profile.equals("postgres")) {
					if (pemailService.existEmailTemplate(email.getName().trim(), email.getId())) {
						res.setStatus("fail");
						res.addError("name", "Nombre de plantilla ya existe");
					}
				}
				
				
			}

			if (email.getTo() == null || ((email.getTo() != null) ? email.getTo() : "").trim().equals("")) {
				res.setStatus("fail");
				res.addError("to", Constant.EMPTY);
			}

			if (email.getName() == null || ((email.getName() != null) ? email.getName() : "").trim().equals("")) {
				res.setStatus("fail");
				res.addError("name", Constant.EMPTY);
			}

			String cc_invalid = "";
			String[] ccAddress = email.getCc().split(",");
			for (int i = 0; i < ccAddress.length; i++) {
				if (!CommonUtils.isValidEmailAddress(ccAddress[i])) {
					cc_invalid += ( cc_invalid.equals("")) ? (ccAddress[i]) : (","+ccAddress[i] );
				}
			}
			if (!cc_invalid.equals("")) {
				res.addError("cc", "dirección(es) inválida(s) " + cc_invalid);
				res.setStatus("fail");
			}

			String to_invalid = "";
			String[] toAddress = email.getTo().split(",");
			for (int i = 0; i < toAddress.length; i++) {
				if (!CommonUtils.isValidEmailAddress(toAddress[i])) {
					to_invalid += (to_invalid.equals("")) ? (toAddress[i]) : (","+toAddress[i] );
				}
			}
			if (!to_invalid.equals("")) {
				res.addError("to", "dirección(es) inválida(s) " + to_invalid);
				res.setStatus("fail");
			}

			if (res.getStatus().equals("fail")) {
				return res;
			}
			if (profile.equals("oracle")) {
				email.setRetry(0);
				email.setCreatedormodify(CommonUtils.getSystemTimestamp());
				email.setState(1);
				email.setUsermodify(getUserLogin().getId());
				emailService.updateEmail(email);
			} else if (profile.equals("postgres")) {
				PEmailTemplate pEmail=new PEmailTemplate();
				pEmail.setCc(email.getCc());
				pEmail.setName(email.getName());
				pEmail.setFrom(email.getFrom());
				pEmail.setTo(email.getTo());
				pEmail.setHtml(email.getHtml());
				pEmail.setSubject(email.getSubject());
				pEmail.setCreatedormodify(CommonUtils.getSystemTimestamp());
				pEmail.setState(1);
				pEmail.setRetry(0);
				pEmail.setUsermodify(getUserLogin().getId());
				pEmail.setId(email.getId());
				pemailService.updateEmail(pEmail);
			}
			

		} catch (Exception e) {
			Sentry.capture(e, "email");
			res.setStatus("exception");
			res.setException("Error al actualizar correo: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteEmail/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteEmail(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			String profile = profileActive();
			if (profile.equals("oracle")) {
				emailService.deleteEmail(id);
			} else if (profile.equals("postgres")) {
				pemailService.deleteEmail(id);
			}
			
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			Sentry.capture(e, "email");
			res.setStatus("exception");
			res.setException("Error al eliminar correo: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}

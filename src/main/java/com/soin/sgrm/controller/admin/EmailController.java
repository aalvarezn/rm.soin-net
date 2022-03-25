package com.soin.sgrm.controller.admin;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.model.pos.PEmailTemplate;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.service.pos.EmailTemplateService;
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
	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		return "/admin/email/email";
	}
	

	@RequestMapping(value = { "editemail","editemail/{id}" }, method = RequestMethod.GET)
	public String update(HttpServletRequest request, Locale locale, Model model, HttpSession session,@PathVariable("id") Optional<Long> id) {
		if(id.isPresent()) {
			try {
				model.addAttribute("email", emailService.findById(id.get()));
				   PEmailTemplate prueba = emailService.findById(id.get());
				   System.out.print(prueba);
			}catch(Exception e ) {
				e.printStackTrace();
			}
		
			
		}	
		
		return "/admin/email/editemail";
	}

	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		JsonSheet<PEmailTemplate> rfcs = new JsonSheet<>();
		try {
			
			rfcs.setData(emailService.findAll());
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rfcs;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse save(HttpServletRequest request, @RequestBody PEmailTemplate addEmailTemplate) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			addEmailTemplate.setDate(CommonUtils.getSystemTimestamp());
			emailService.save(addEmailTemplate);
            res.setData(addEmailTemplate.getId().toString());
			res.setMessage("Plantilla agregada!");
		} catch (Exception e) {
			Sentry.capture(e, "emailTemplate");
			res.setStatus("exception");
			res.setMessage("Error al agregar plantilla!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody PEmailTemplate uptEmailTemplate) throws IOException {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			uptEmailTemplate.setDate(CommonUtils.getSystemTimestamp());
			emailService.update(uptEmailTemplate);

			res.setMessage("Plantilla modificada!");
		}catch (Exception e) {
			Sentry.capture(e, "emailTemplate");
			res.setStatus("exception");
			res.setMessage("Error al modificar plantilla!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse delete(@PathVariable Long id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			emailService.delete(id);
			res.setMessage("Plantilla eliminada!");
		} catch (Exception e) {
			Sentry.capture(e, "emailTemplate");
			res.setStatus("exception");
			res.setMessage("Error al eliminar plantilla!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(path = "/emailTest", method = RequestMethod.POST)
	public @ResponseBody JsonResponse emailTest(HttpServletRequest request,
			@Valid @ModelAttribute("EmailTemplate") PEmailTemplate email, BindingResult errors, ModelMap model,
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
					cc_invalid += ((i + 1) == ccAddress.length) ? (ccAddress[i]) : (ccAddress[i] + ",");
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
					to_invalid += ((i + 1) == toAddress.length) ? (toAddress[i]) : (toAddress[i] + ",");
				}
			}
			if (!to_invalid.equals("")) {
				res.addError("to", "dirección(es) inválida(s) " + to_invalid);
				res.setStatus("fail");
			}

			if (res.getStatus().equals("fail")) {
				return res;
			}
			emailService.sendMail(email.getTo(), email.getCc(), email.getSubject(), email.getBody());
			res.setMessage("Correo enviado!");
		} catch (Exception e) {
			Sentry.capture(e, "email");
			res.setStatus("exception");
			res.setException("Error al enviar correo: " + e.toString());
			res.setMessage("Ocurrio un error al enviar correo");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;

	}



}

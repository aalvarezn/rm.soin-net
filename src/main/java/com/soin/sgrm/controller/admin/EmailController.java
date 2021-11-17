package com.soin.sgrm.controller.admin;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.soin.sgrm.service.EmailTemplateService;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.JsonResponse;

@Controller
@RequestMapping("/admin/email")
public class EmailController extends BaseController {

	@Autowired
	EmailTemplateService emailService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("emails", emailService.listAll());
		return "/admin/email/email";
	}

	@RequestMapping(value = { "/editarEmail-{id}" }, method = RequestMethod.GET)
	public String editEmail(@PathVariable Integer id, HttpServletRequest request, Locale locale, Model model,
			HttpSession session) {
		model.addAttribute("email", emailService.findById(id));
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
				if (!isValidEmailAddress(ccAddress[i])) {
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
				if (!isValidEmailAddress(toAddress[i])) {
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
			emailService.sendMail(email.getTo(), email.getCc(), email.getSubject(), email.getHtml());

		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al enviar correo: " + e.toString());
			logs("ADMIN_ERROR", "Error al enviar correo: " + getErrorFormat(e));
		}
		return res;

	}

	@RequestMapping(path = "/saveEmail", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveEmail(HttpServletRequest request,
			@Valid @ModelAttribute("EmailTemplate") EmailTemplate email, BindingResult errors, ModelMap model,
			Locale locale, HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
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
				if (emailService.existEmailTemplate(email.getName().trim())) {
					res.setStatus("fail");
					res.addError("name", "Nombre de plantilla ya existe");
				}
			}
			if (res.getStatus().equals("fail")) {
				return res;
			}
			email.setRetry(0);
			email.setCreatedormodify(getSystemTimestamp());
			email.setState(0);
			email.setUsermodify(getUserId());
			emailService.saveEmail(email);
			res.setObj(email);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al crear correo: " + e.toString());
			logs("ADMIN_ERROR", "Error al crear correo: " + getErrorFormat(e));
			e.printStackTrace();
		}
		return res;
	}

	@RequestMapping(path = "/updateEmail", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateEmail(HttpServletRequest request,
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

			if (email.getName() != null) {
				if (emailService.existEmailTemplate(email.getName().trim(), email.getId())) {
					res.setStatus("fail");
					res.addError("name", "Nombre de plantilla ya existe");
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
				if (!isValidEmailAddress(ccAddress[i])) {
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
				if (!isValidEmailAddress(toAddress[i])) {
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

			email.setRetry(0);
			email.setCreatedormodify(getSystemTimestamp());
			email.setState(1);
			email.setUsermodify(getUserId());
			emailService.updateEmail(email);

		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al actualizar correo: " + e.toString());
			logs("ADMIN_ERROR", "Error al actualizar correo: " + getErrorFormat(e));
			e.printStackTrace();
		}
		return res;
	}

	@RequestMapping(value = "/deleteEmail/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteEmail(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			emailService.deleteEmail(id);
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar correo: " + e.toString());
			logs("ADMIN_ERROR", "Error al eliminar correo: " + getErrorFormat(e));
			e.printStackTrace();
		}
		return res;
	}

}

package com.soin.sgrm.controller.admin;

import java.util.Locale;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.controller.ReleaseRequestController;
import com.soin.sgrm.model.Authority;
import com.soin.sgrm.service.AuthorityService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.exception.Sentry;

@Controller
@RequestMapping("/admin/authority")
public class AuthorityController extends BaseController {

	public static final Logger logger = Logger.getLogger(AuthorityController.class);

	@Autowired
	AuthorityService authorityService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("authoritys", authorityService.list());
		model.addAttribute("authority", new Authority());
		return "/admin/authority/authority";
	}

	@RequestMapping(value = "/findAuthority/{id}", method = RequestMethod.GET)
	public @ResponseBody Authority findAuthority(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		try {
			Authority authority = authorityService.findById(id);
			return authority;
		} catch (Exception e) {
			Sentry.capture(e, "authority");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}

	@RequestMapping(path = "/saveAuthority", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveAuthority(HttpServletRequest request,

			@Valid @ModelAttribute("Authority") Authority authority, BindingResult errors, ModelMap model,
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
			if (res.getStatus().equals("success")) {
				authorityService.save(authority);
				res.setObj(authority);
			}
		} catch (Exception e) {
			Sentry.capture(e, "authority");
			res.setStatus("exception");
			res.setException("Error al crear role: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/updateAuthority", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateAuthority(HttpServletRequest request,
			@Valid @ModelAttribute("Authority") Authority authority, BindingResult errors, ModelMap model,
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
			if (res.getStatus().equals("success")) {
				Authority authorityOrigin = authorityService.findById(authority.getId());
				authorityOrigin.setName(authority.getName());
				authorityService.update(authorityOrigin);
				res.setObj(authority);
			}
		} catch (Exception e) {
			Sentry.capture(e, "authority");
			res.setStatus("exception");
			res.setException("Error al modificar role: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/deleteAuthority/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse deleteAuthority(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			authorityService.delete(id);
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar role: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar role: Existen referencias que debe eliminar antes");
			} else {
				Sentry.capture(e, "authority");
			}
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}
}

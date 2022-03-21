package com.soin.sgrm.controller;

import java.sql.SQLException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;

import com.soin.sgrm.exception.Sentry;

import com.soin.sgrm.model.Password;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.service.UserInfoService;
import com.soin.sgrm.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController extends BaseController {

	public static final Logger logger = Logger.getLogger(ProfileController.class);

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	UserService userService;

	@Autowired
	UserInfoService userInfoService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(HttpServletRequest request, Locale locale, Model model) {
		model.addAttribute("user", userService.getUserByUsername(getUserLogin().getUsername()));
		model.addAttribute("details", userInfoService.getUserByUsername(getUserLogin().getUsername()));
		return "/profile/profile";
	}

//	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
//	public @ResponseBody JsonResponse changePassword(HttpServletResponse response,
//			@ModelAttribute("Password") Password p, BindingResult errors, ModelMap model) {
//		JsonResponse res = new JsonResponse();
//		try {
//			UserInfo user = userInfoService.findUserInfoById(getUserLogin().getId());
//			res.setStatus("success");
//
//			if (!p.getNewPassword().equals(p.getConfirmPassword())) {
//				res.setStatus("fail");
//				res.setException("Las nuevas contraseñas no coinciden.");
//				return res;
//			}
//
//			if (!encoder.matches(p.getOldPassword(), user.getPassword())) {
//				res.setStatus("fail");
//				res.setException("La contraseña anterior es incorrecta.");
//				return res;
//			}
//			if (CommonUtils.isNumeric(p.getNewPassword())) {
//				res.setStatus("fail");
//				res.setException("La contraseña no puede contener sólo números.");
//				return res;
//			}
//
//			if (res.getStatus().equals("success")) {
//				String newPassword = encoder.encode(p.getNewPassword());
//				user.setPassword(newPassword);
//				userInfoService.changePassword(user);
//			}
//		} catch (SQLException ex) {
//			Sentry.capture(ex, "profile");
//			res.setStatus("exception");
//			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
//		} catch (Exception e) {
//			Sentry.capture(e, "profile");
//			res.setStatus("exception");
//			res.setException(e.getMessage());
//		}
//		return res;
//	}

}

package com.soin.sgrm.controller;

import java.sql.SQLException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.model.Password;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.service.UserInfoService;
import com.soin.sgrm.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController extends BaseController {

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	UserService userService;

	@Autowired
	UserInfoService userInfoService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(HttpServletRequest request, Locale locale, Model model) {
		String name = getUserName();
		model.addAttribute("user", userService.getUserByUsername(name));
		model.addAttribute("details", userInfoService.getUserByUsername(name));
		return "/profile/profile";
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public @ResponseBody JsonResponse changePassword(HttpServletResponse response, @ModelAttribute("Password") Password p,
			BindingResult errors, ModelMap model) {
		JsonResponse res = new JsonResponse();
		try {
			UserInfo user = getUseInfo();
			res.setStatus("success");
			
			if (!p.getNewPassword().equals(p.getConfirmPassword())) {
				res.setStatus("fail");
				res.setException("Las nuevas contraseñas no coinciden.");
			}
			
			if (!encoder.matches(p.getOldPassword(), user.getPassword())) {
				res.setStatus("fail");
				res.setException("La contraseña anterior es incorrecta.");
			}
			
			if(res.getStatus().equals("success")) {
				String newPassword = encoder.encode(p.getNewPassword());
				user.setPassword(newPassword);
				userInfoService.changePassword(user);
			}
		}catch (SQLException ex) {
			res.setStatus("exception");
			res.setException("Problemas de conexión con la base de datos, favor intente más tarde.");
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException(e.getMessage());
		}
		return res;
	}

}

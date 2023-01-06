package com.soin.sgrm.controller.admin;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Authority;
import com.soin.sgrm.model.Password;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.service.AuthorityService;
import com.soin.sgrm.service.UserInfoService;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/user")
public class UserController extends BaseController {

	public static final Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	UserInfoService userService;

	@Autowired
	AuthorityService authorityService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("listUser", userService.list());
		model.addAttribute("listRoles", authorityService.list());
		return "/admin/user/user";
	}

	@RequestMapping(value = "/findUser/{id}", method = RequestMethod.GET)
	public @ResponseBody UserInfo findSystemConfig(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		UserInfo user = null;
		try {
			user = userService.findUserInfoById(id);
			return user;
		} catch (Exception e) {
			Sentry.capture(e, "user");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateUserInfo(HttpServletRequest request,
			@Valid @ModelAttribute("UserInfo") UserInfo user, BindingResult errors, ModelMap model, Locale locale,
			HttpSession session) {
		JsonResponse res = new JsonResponse();
		UserInfo userInfo = null;
		try {
			res.setStatus("success");

			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
				return res;
			}

			userInfo = userService.findUserInfoById(user.getId());
			userInfo.setUsername(user.getUsername());
			userInfo.setShortName(user.getShortName());
			userInfo.setFullName(user.getFullName());
			userInfo.setEmailAddress(user.getEmailAddress());
			userInfo.setGitusername(user.getGitusername());
			Authority temp = null;
			Set<Authority> authsNews = new HashSet<>();
			for (Integer index : user.getRolesId()) {
				temp = authorityService.findById(index);
				if (temp != null) {
					authsNews.add(temp);
				}
			}
			userInfo.checkAuthoritiesExists(authsNews);

			if (!userService.uniqueGitUsername(userInfo)) {
				res.setStatus("exception");
				res.setException("El nombre de usuario de git ya se encuentra en uso ");
			}
			
			if (!userService.uniqueUsername(userInfo)) {
				res.setStatus("exception");
				res.setException("El nombre de usuario ya se encuentra en uso ");
			}

			if (res.getStatus().equals("success")) {
				userService.updateUserInfo(userInfo);
				res.setObj(userInfo);
			}

		} catch (Exception e) {
			Sentry.capture(e, "user");
			res.setStatus("exception");
			res.setException("Error al actualizar usuario: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/saveUser", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveUserInfo(HttpServletRequest request,
			@Valid @ModelAttribute("UserInfo") UserInfo userInfo, BindingResult errors, ModelMap model, Locale locale,
			HttpSession session) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");

			if (errors.hasErrors()) {
				for (FieldError error : errors.getFieldErrors()) {
					res.addError(error.getField(), error.getDefaultMessage());
				}
				res.setStatus("fail");
				return res;
			}
			Authority temp = null;
			Set<Authority> authsNews = new HashSet<>();
			for (Integer index : userInfo.getRolesId()) {
				temp = authorityService.findById(index);
				if (temp != null) {
					authsNews.add(temp);
				}
			}

			userInfo.setActive(true);
			userInfo.setIsReleaseManager(0);
			userInfo.setIsSuperUser(0);
			userInfo.setStaff(false);
			userInfo.setDateJoined(CommonUtils.getSystemTimestamp());

			if (!userService.uniqueUsername(userInfo)) {
				res.setStatus("exception");
				res.setException("El nombre de usuario ya se encuentra en uso ");
			}
			
			if (!userService.uniqueGitUsername(userInfo)) {
				res.setStatus("exception");
				res.setException("El nombre de usuario ya se encuentra en uso ");
			}

			if (res.getStatus().equals("success")) {
				userService.saveUserInfo(userInfo);
				res.setObj(userInfo);
			}

		} catch (Exception e) {
			Sentry.capture(e, "user");
			res.setStatus("exception");
			res.setException("Error al guardar usuario: " + e.toString());
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public @ResponseBody JsonResponse changePassword(HttpServletResponse response,
			@ModelAttribute("Password") Password p, BindingResult errors, ModelMap model) {
		JsonResponse res = new JsonResponse();
		UserInfo user;
		try {
			user = userService.findUserInfoById(p.getUserId());
			res.setStatus("success");

			if (!p.getNewPassword().equals(p.getConfirmPassword())) {
				res.setStatus("fail");
				res.setException("Las contraseñas no coinciden.");
			}
			if (res.getStatus().equals("success")) {
				String newPassword = encoder.encode(p.getNewPassword());
				user.setPassword(newPassword);
				userService.changePassword(user);
			}
		} catch (Exception e) {
			Sentry.capture(e, "user");
			res.setStatus("exception");
			res.setException(e.getMessage());
		}
		return res;
	}

	@RequestMapping(value = "/softDelete", method = RequestMethod.POST)
	public @ResponseBody JsonResponse softDelete(HttpServletRequest request, HttpServletResponse response,
			Locale locale, Model model, HttpSession session) {
		JsonResponse res = new JsonResponse();
		UserInfo user;
		try {
			Integer userId = Integer.parseInt(request.getParameter("userId"));
			user = userService.findUserInfoById(userId);
			user.setActive(!user.getActive());
			userService.softDelete(user);
			res.setObj(user.getActive());
			res.setStatus("success");

		} catch (Exception e) {
			Sentry.capture(e, "user");
			res.setStatus("exception");
			res.setException(e.getMessage());
		}
		return res;
	}

	@RequestMapping(value = "/removeUser/{id}", method = RequestMethod.DELETE)
	public @ResponseBody JsonResponse removeUser(@PathVariable Integer id, Model model) {
		JsonResponse res = new JsonResponse();
		try {
			userService.delete(id);
			res.setStatus("success");
			res.setObj(id);
		} catch (Exception e) {
			res.setStatus("exception");
			res.setException("Error al eliminar usuario: " + e.getCause().getCause().getCause().getMessage() + ":"
					+ e.getMessage());

			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
				res.setException("Error al eliminar usuario: Existen referencias que debe eliminar antes");
			} else {
				Sentry.capture(e, "user");
			}

			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}

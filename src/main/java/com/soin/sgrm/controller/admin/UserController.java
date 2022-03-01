package com.soin.sgrm.controller.admin;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.security.UserLogin;
import com.soin.sgrm.service.pos.AuthorityService;
import com.soin.sgrm.service.UserInfoService;
import com.soin.sgrm.service.pos.UserService;
import com.soin.sgrm.utils.JsonResponse;
import com.soin.sgrm.utils.MyLevel;

@Controller
@RequestMapping("/admin/user")
public class UserController extends BaseController {

	public static final Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	UserService user;

	@Autowired
	UserInfoService userService;

	@Autowired
	AuthorityService authority;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("roles", authority.findAll());
		return "/admin/user/user";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		UserLogin userLogin = getUserLogin();

		JsonSheet<PUser> list = new JsonSheet<>();
		try {
			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			Integer sStatus = (request.getParameter("sStatus").trim().equals("")) ? null
					: Integer.parseInt(request.getParameter("sStatus"));

			String sSearch = request.getParameter("sSearch");
			String dateRange = request.getParameter("dateRange");

			list = user.findAll(sEcho, iDisplayStart, iDisplayLength, sSearch, sStatus, dateRange);
			list.getData().removeIf(ele -> (ele.getUserName().toLowerCase()).equals(userLogin.getUsername()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse update(HttpServletRequest request, @RequestBody PUser uptUser) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			user.update(uptUser);

			res.setMessage("Usuario modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "user");
			res.setStatus("exception");
			res.setMessage("Error al modificar usuario!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

//	@RequestMapping(value = "/findUser/{id}", method = RequestMethod.GET)
//	public @ResponseBody UserInfo findSystemConfig(@PathVariable Integer id, HttpServletRequest request, Locale locale,
//			Model model, HttpSession session) {
//		UserInfo user = null;
//		try {
//			user = userService.findUserInfoById(id);
//			return user;
//		} catch (Exception e) {
//			Sentry.capture(e, "user");
//			logger.log(MyLevel.RELEASE_ERROR, e.toString());
//			return null;
//		}
//	}
//
//	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
//	public @ResponseBody JsonResponse updateUserInfo(HttpServletRequest request,
//			@Valid @ModelAttribute("UserInfo") UserInfo user, BindingResult errors, ModelMap model, Locale locale,
//			HttpSession session) {
//		JsonResponse res = new JsonResponse();
//		UserInfo userInfo = null;
//		try {
//			res.setStatus("success");
//
//			if (errors.hasErrors()) {
//				for (FieldError error : errors.getFieldErrors()) {
//					res.addError(error.getField(), error.getDefaultMessage());
//				}
//				res.setStatus("fail");
//				return res;
//			}
//
//			userInfo = userService.findUserInfoById(user.getId());
//			userInfo.setUsername(user.getUsername());
//			userInfo.setShortName(user.getShortName());
//			userInfo.setFullName(user.getFullName());
//			userInfo.setEmailAddress(user.getEmailAddress());
//			Authority temp = null;
//			Set<Authority> authsNews = new HashSet<>();
//			for (Integer index : user.getRolesId()) {
//				temp = authorityService.findById(index);
//				if (temp != null) {
//					authsNews.add(temp);
//				}
//			}
//			userInfo.checkAuthoritiesExists(authsNews);
//
//			if (!userService.uniqueUsername(userInfo)) {
//				res.setStatus("exception");
//				res.setException("El nombre de usuario ya se encuentra en uso ");
//			}
//
//			if (res.getStatus().equals("success")) {
//				userService.updateUserInfo(userInfo);
//				res.setObj(userInfo);
//			}
//
//		} catch (Exception e) {
//			Sentry.capture(e, "user");
//			res.setStatus("exception");
//			res.setException("Error al actualizar usuario: " + e.toString());
//			logger.log(MyLevel.RELEASE_ERROR, e.toString());
//		}
//		return res;
//	}
//
//	@RequestMapping(value = "/saveUser", method = RequestMethod.POST)
//	public @ResponseBody JsonResponse saveUserInfo(HttpServletRequest request,
//			@Valid @ModelAttribute("UserInfo") UserInfo userInfo, BindingResult errors, ModelMap model, Locale locale,
//			HttpSession session) {
//		JsonResponse res = new JsonResponse();
//		try {
//			res.setStatus("success");
//
//			if (errors.hasErrors()) {
//				for (FieldError error : errors.getFieldErrors()) {
//					res.addError(error.getField(), error.getDefaultMessage());
//				}
//				res.setStatus("fail");
//				return res;
//			}
//			Authority temp = null;
//			Set<Authority> authsNews = new HashSet<>();
//			for (Integer index : userInfo.getRolesId()) {
//				temp = authorityService.findById(index);
//				if (temp != null) {
//					authsNews.add(temp);
//				}
//			}
//
//			userInfo.setActive(true);
//			userInfo.setIsReleaseManager(0);
//			userInfo.setIsSuperUser(0);
//			userInfo.setStaff(false);
//			userInfo.setDateJoined(CommonUtils.getSystemTimestamp());
//
//			if (!userService.uniqueUsername(userInfo)) {
//				res.setStatus("exception");
//				res.setException("El nombre de usuario ya se encuentra en uso ");
//			}
//
//			if (res.getStatus().equals("success")) {
//				userService.saveUserInfo(userInfo);
//				res.setObj(userInfo);
//			}
//
//		} catch (Exception e) {
//			Sentry.capture(e, "user");
//			res.setStatus("exception");
//			res.setException("Error al guardar usuario: " + e.toString());
//			logger.log(MyLevel.RELEASE_ERROR, e.toString());
//		}
//		return res;
//	}
//
//	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
//	public @ResponseBody JsonResponse changePassword(HttpServletResponse response,
//			@ModelAttribute("Password") Password p, BindingResult errors, ModelMap model) {
//		JsonResponse res = new JsonResponse();
//		UserInfo user;
//		try {
//			user = userService.findUserInfoById(p.getUserId());
//			res.setStatus("success");
//
//			if (!p.getNewPassword().equals(p.getConfirmPassword())) {
//				res.setStatus("fail");
//				res.setException("Las contraseñas no coinciden.");
//			}
//			if (res.getStatus().equals("success")) {
//				String newPassword = encoder.encode(p.getNewPassword());
//				user.setPassword(newPassword);
//				userService.changePassword(user);
//			}
//		} catch (Exception e) {
//			Sentry.capture(e, "user");
//			res.setStatus("exception");
//			res.setException(e.getMessage());
//		}
//		return res;
//	}
//
//	@RequestMapping(value = "/softDelete", method = RequestMethod.POST)
//	public @ResponseBody JsonResponse softDelete(HttpServletRequest request, HttpServletResponse response,
//			Locale locale, Model model, HttpSession session) {
//		JsonResponse res = new JsonResponse();
//		UserInfo user;
//		try {
//			Integer userId = Integer.parseInt(request.getParameter("userId"));
//			user = userService.findUserInfoById(userId);
//			user.setActive(!user.getActive());
//			userService.softDelete(user);
//			res.setObj(user.getActive());
//			res.setStatus("success");
//
//		} catch (Exception e) {
//			Sentry.capture(e, "user");
//			res.setStatus("exception");
//			res.setException(e.getMessage());
//		}
//		return res;
//	}
//
//	@RequestMapping(value = "/removeUser/{id}", method = RequestMethod.DELETE)
//	public @ResponseBody JsonResponse removeUser(@PathVariable Integer id, Model model) {
//		JsonResponse res = new JsonResponse();
//		try {
//			userService.delete(id);
//			res.setStatus("success");
//			res.setObj(id);
//		} catch (Exception e) {
//			res.setStatus("exception");
//			res.setException("Error al eliminar usuario: " + e.getCause().getCause().getCause().getMessage() + ":"
//					+ e.getMessage());
//
//			if (e.getCause().getCause().getCause().getMessage().contains("ORA-02292")) {
//				res.setException("Error al eliminar usuario: Existen referencias que debe eliminar antes");
//			} else {
//				Sentry.capture(e, "user");
//			}
//
//			logger.log(MyLevel.RELEASE_ERROR, e.toString());
//		}
//		return res;
//	}

}

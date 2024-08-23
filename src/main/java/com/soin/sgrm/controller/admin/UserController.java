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
import org.springframework.core.env.Environment;
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
import com.soin.sgrm.model.pos.PAuthority;
import com.soin.sgrm.model.pos.PUserInfo;
import com.soin.sgrm.service.AuthorityService;
import com.soin.sgrm.service.UserInfoService;
import com.soin.sgrm.service.pos.PAuthorityService;
import com.soin.sgrm.service.pos.PUserInfoService;
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
	PUserInfoService puserService;

	@Autowired
	AuthorityService authorityService;
	
	@Autowired
	PAuthorityService pauthorityService;

	private final Environment environment;

	@Autowired
	public UserController(Environment environment) {
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
			model.addAttribute("listUser", userService.list());
			model.addAttribute("listRoles", authorityService.list());
		} else if (profile.equals("postgres")) {
			model.addAttribute("listUser", puserService.list());
			model.addAttribute("listRoles", pauthorityService.list());
		}
	
		return "/admin/user/user";
	}

	@RequestMapping(value = "/findUser/{id}", method = RequestMethod.GET)
	public @ResponseBody Object findSystemConfig(@PathVariable Integer id, HttpServletRequest request, Locale locale,
			Model model, HttpSession session) {
		
		try {
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				UserInfo user = null;
				user = userService.findUserInfoById(id);
				return user;
			} else if (profile.equals("postgres")) {
				PUserInfo user = null;
				user = puserService.findUserInfoById(id);
				return user;
			}
			
			
		} catch (Exception e) {
			Sentry.capture(e, "user");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			return null;
		}
		return null;
	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public @ResponseBody JsonResponse updateUserInfo(HttpServletRequest request,
			@Valid @ModelAttribute("UserInfo") UserInfo user, BindingResult errors, ModelMap model, Locale locale,
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

			String profile = profileActive();
			if (profile.equals("oracle")) {
				UserInfo userInfo = null;
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
			} else if (profile.equals("postgres")) {
				PUserInfo puserInfo = null;
				puserInfo = puserService.findUserInfoById(user.getId());
				puserInfo.setUsername(user.getUsername());
				puserInfo.setShortName(user.getShortName());
				puserInfo.setFullName(user.getFullName());
				puserInfo.setEmailAddress(user.getEmailAddress());
				puserInfo.setGitusername(user.getGitusername());
				PAuthority temp = null;
				Set<PAuthority> pauthsNews = new HashSet<>();
				for (Integer index : user.getRolesId()) {
					temp = pauthorityService.findById(index);
					if (temp != null) {
						pauthsNews.add(temp);
					}
				}
				puserInfo.checkAuthoritiesExists(pauthsNews);

				if(!puserInfo.getGitusername().isEmpty()) {
					if (!puserService.uniqueGitUsername(puserInfo)) {
						res.setStatus("exception");
						res.setException("El nombre de usuario de git ya se encuentra en uso ");
					}
				}
			
				if (!puserService.uniqueUsername(puserInfo)) {
					res.setStatus("exception");
					res.setException("El nombre de usuario ya se encuentra en uso ");
				}

				if (res.getStatus().equals("success")) {
					puserService.updateUserInfo(puserInfo);
					res.setObj(puserInfo);
				}
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

			
			String profile = profileActive();
			if (profile.equals("oracle")) {
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
					res.setException("El nombre de usuario de git ya se encuentra en uso ");
				}

				if (res.getStatus().equals("success")) {
					userService.saveUserInfo(userInfo);
					res.setObj(userInfo);
				}
			} else if (profile.equals("postgres")) {
				PAuthority temp = null;
				PUserInfo puserInfo=new PUserInfo();
				Set<PAuthority> pauthsNews = new HashSet<>();
				for (Integer index : userInfo.getRolesId()) {
					temp = pauthorityService.findById(index);
					if (temp != null) {
						pauthsNews.add(temp);
					}
				}

				puserInfo.setActive(true);
				puserInfo.setIsReleaseManager(0);
				puserInfo.setIsSuperUser(0);
				puserInfo.setStaff(false);
				puserInfo.setDateJoined(CommonUtils.getSystemTimestamp());
				puserInfo.setUsername(userInfo.getUsername());
				puserInfo.setGitusername(userInfo.getGitusername());
				puserInfo.setShortName(userInfo.getShortName());
				puserInfo.setEmailAddress(userInfo.getEmailAddress());
				puserInfo.setFullName(userInfo.getFullName());
				if (!puserService.uniqueUsername(puserInfo)) {
					res.setStatus("exception");
					res.setException("El nombre de usuario ya se encuentra en uso ");
				}
				
				if (!puserService.uniqueGitUsername(puserInfo)) {
					res.setStatus("exception");
					res.setException("El nombre de usuario de git ya se encuentra en uso ");
				}

				if (res.getStatus().equals("success")) {
					puserService.saveUserInfo(puserInfo);
					res.setObj(puserInfo);
				}
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
		
		try {
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				UserInfo user;
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
			} else if (profile.equals("postgres")) {
				PUserInfo puser;
				puser = puserService.findUserInfoById(p.getUserId());
				res.setStatus("success");

				if (!p.getNewPassword().equals(p.getConfirmPassword())) {
					res.setStatus("fail");
					res.setException("Las contraseñas no coinciden.");
				}
				if (res.getStatus().equals("success")) {
					String newPassword = encoder.encode(p.getNewPassword());
					puser.setPassword(newPassword);
					puserService.changePassword(puser);
				}
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
		
		try {
			String profile = profileActive();
			if (profile.equals("oracle")) {
				UserInfo user;
				Integer userId = Integer.parseInt(request.getParameter("userId"));
				user = userService.findUserInfoById(userId);
				user.setActive(!user.getActive());
				userService.softDelete(user);
				res.setObj(user.getActive());
				res.setStatus("success");
			} else if (profile.equals("postgres")) {
				PUserInfo puser;
				Integer userId = Integer.parseInt(request.getParameter("userId"));
				puser = puserService.findUserInfoById(userId);
				puser.setActive(!puser.getActive());
				puserService.softDelete(puser);
				res.setObj(puser.getActive());
				res.setStatus("success");
			}


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
			
			String profile = profileActive();
			if (profile.equals("oracle")) {
				userService.delete(id);
				res.setStatus("success");
				res.setObj(id);
			} else if (profile.equals("postgres")) {
				puserService.delete(id);
				res.setStatus("success");
				res.setObj(id);
			}
			
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

package com.soin.sgrm.controller.admin;

import java.util.List;
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

import com.google.common.collect.Sets;
import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.model.pos.PAuthority;
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
	UserService userService;

	@Autowired
	AuthorityService authorityService;

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Locale locale, Model model, HttpSession session) {
		model.addAttribute("roles", authorityService.findAll());
		return "/admin/user/user";
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public @ResponseBody JsonSheet list(HttpServletRequest request, Locale locale, Model model) {
		PUser user = getUserLogin();

		JsonSheet<PUser> list = new JsonSheet<>();
		try {
			Integer sEcho = Integer.parseInt(request.getParameter("sEcho"));
			Integer iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
			Integer iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
			Integer sStatus = (request.getParameter("sStatus").trim().equals("")) ? null
					: Integer.parseInt(request.getParameter("sStatus"));

			String sSearch = request.getParameter("sSearch");
			String dateRange = request.getParameter("dateRange");

			list = userService.findAll(sEcho, iDisplayStart, iDisplayLength, sSearch, sStatus, dateRange);
			list.getData().removeIf(ele -> (ele.getUserName().toLowerCase()).equals(user.getUsername()));

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
			PUser user = userService.findById(uptUser.getId());
			user.setUserName(uptUser.getUserName());
			user.setName(uptUser.getName());
			user.setEmail(uptUser.getEmail());
			user.setActive(uptUser.getActive());
			List<PAuthority> roles = authorityService.findByCode(uptUser.getStrRoles());
			user.setRoles(Sets.newHashSet(roles));
			userService.update(user);
			res.setMessage("Usuario modificado!");
		} catch (Exception e) {
			Sentry.capture(e, "user");
			res.setStatus("exception");
			res.setMessage("Error al modificar usuario!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public @ResponseBody JsonResponse saveUser(HttpServletRequest request, @RequestBody PUser addUser) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			List<PAuthority> roles = authorityService.findByCode(addUser.getStrRoles());
			addUser.setRoles(Sets.newHashSet(roles));
			userService.save(addUser);

			res.setMessage("Usuario creado!");
		} catch (Exception e) {
			Sentry.capture(e, "user");
			res.setStatus("exception");
			res.setMessage("Error al crear usuario!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

	@RequestMapping(value = "/password", method = RequestMethod.PUT)
	public @ResponseBody JsonResponse password(HttpServletRequest request, @RequestBody PUser uptUser) {
		JsonResponse res = new JsonResponse();
		try {
			res.setStatus("success");
			PUser user = userService.findById(uptUser.getId());
			if (uptUser.getNewPassword().equals(uptUser.getConfirmPassword())) {
				String newPassword = encoder.encode(uptUser.getNewPassword());
				user.setPassword(newPassword);
				userService.update(user);
				res.setMessage("Contraseña modificada!");
			} else {
				res.setStatus("error");
				res.setMessage("Las contraseñas no coinciden!");
			}
		} catch (Exception e) {
			Sentry.capture(e, "user");
			res.setStatus("exception");
			res.setMessage("Error al modificar contraseña!");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return res;
	}

}

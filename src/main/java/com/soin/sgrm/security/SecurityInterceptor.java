package com.soin.sgrm.security;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.soin.sgrm.controller.BaseController;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.model.SystemUser;
import com.soin.sgrm.service.ReleaseService;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.service.UserInfoService;
import com.soin.sgrm.utils.MyLevel;

@Component
public class SecurityInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private UserInfoService loginService;
	@Autowired
	private ReleaseService releaseService;
	@Autowired
	private SystemService systemService;

	public static final Logger logger = Logger.getLogger(SecurityInterceptor.class);

	@SuppressWarnings("deprecation")
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		Integer jsCache = (int) (Math.random() * 10) + 0;
		request.setAttribute("jsVersion", jsCache);

		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (!auth.getPrincipal().equals("anonymousUser")) {

				String pathInfo = request.getServletPath();
				if (pathInfo.equalsIgnoreCase("/500") || pathInfo.equalsIgnoreCase("/logout")
						|| pathInfo.equalsIgnoreCase("/login") || pathInfo.equalsIgnoreCase("/info")) {
					return true;
				}

				UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal();
				UserInfo user = loginService.getUserByUsername(userDetails.getUsername());

				if (user == null && auth != null) {
					new SecurityContextLogoutHandler().logout(request, response, auth);
					return true;
				}

				request.setAttribute("name", (user.getFullName() != null) ? user.getFullName().toUpperCase()
						: user.getUsername().toUpperCase());
				request.setAttribute("userInfo", user);

				if (user.getIsReleaseManager() == 0) { // Si no es release manager

					List<SystemUser> systems = systemService.listSystemByUser(user.getUsername());
					Map<String, Integer> userC = new HashMap<String, Integer>();
					userC.put("certification",
							releaseService.countByType(user.getUsername(), "Certificacion", 1, null));
					userC.put("draft", releaseService.countByType(user.getUsername(), "Borrador", 1, null));
					userC.put("review", releaseService.countByType(user.getUsername(), "En Revision", 1, null));
					userC.put("completed", releaseService.countByType(user.getUsername(), "Completado", 1, null));
					request.setAttribute("userC", userC);

					Object[] ids = systemService.myTeams(user.getUsername());
					Map<String, Integer> teamC = new HashMap<String, Integer>();

					if (systems.size() == 0) {
						teamC.put("certification", 0);
						teamC.put("draft", 0);
						teamC.put("review", 0);
						teamC.put("completed", 0);
						request.setAttribute("userC", userC);
					} else {
						teamC.put("certification",
								releaseService.countByType(user.getUsername(), "Certificacion", 2, ids));
						teamC.put("draft", releaseService.countByType(user.getUsername(), "Borrador", 2, ids));
						teamC.put("review", releaseService.countByType(user.getUsername(), "En Revision", 2, ids));
						teamC.put("completed", releaseService.countByType(user.getUsername(), "Completado", 2, ids));
					}
					request.setAttribute("teamC", teamC);

					Map<String, Integer> systemC = new HashMap<String, Integer>();
					systemC.put("certification",
							releaseService.countByType(user.getUsername(), "Certificacion", 3, null));
					systemC.put("draft", releaseService.countByType(user.getUsername(), "Borrador", 3, null));
					systemC.put("review", releaseService.countByType(user.getUsername(), "En Revision", 3, null));
					systemC.put("completed", releaseService.countByType(user.getUsername(), "Completado", 3, null));
					request.setAttribute("systemC", systemC);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(MyLevel.SYSTEM_ERROR, e.toString());
			request.setAttribute("errorType", e.getCause().toString());
			response.sendRedirect(request.getContextPath() + "/500" + "?errorType=" + e.getCause().toString());
			throw e;
		}
		return true;
	}

}

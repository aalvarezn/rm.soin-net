package com.soin.sgrm.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.service.UserInfoService;
import com.soin.sgrm.utils.MyLevel;

import com.soin.sgrm.exception.Sentry;

@Component
public class SecurityInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private UserInfoService loginService;

	public static final Logger logger = Logger.getLogger(SecurityInterceptor.class);

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

			}
		} catch (Exception e) {
			Sentry.capture(e, "SYSTEM");
			logger.log(MyLevel.SYSTEM_ERROR, e.toString());
			request.setAttribute("errorType", e.getCause().toString());
			response.sendRedirect(request.getContextPath() + "/500" + "?errorType=" + e.getCause().toString());
			throw e;
		}
		return true;
	}

}

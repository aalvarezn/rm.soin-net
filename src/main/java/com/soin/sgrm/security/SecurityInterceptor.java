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
import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.service.UserInfoService;
import com.soin.sgrm.utils.MyLevel;

import com.soin.sgrm.exception.Sentry;

@Component
public class SecurityInterceptor extends HandlerInterceptorAdapter {

	public static final Logger logger = Logger.getLogger(SecurityInterceptor.class);

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		Integer jsCache = (int) (Math.random() * 10) + 0;
		request.setAttribute("jsVersion", jsCache);

		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (!auth.getPrincipal().equals("anonymousUser")) {
				PUser user = (PUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				if (user == null && auth != null) {
					new SecurityContextLogoutHandler().logout(request, response, auth);
					return true;
				}
				request.setAttribute("userInfo", user);
			}
		} catch (Exception e) {
			request.setAttribute("errorType", e.getCause().toString());
			response.sendRedirect(request.getContextPath() + "/500" + "?errorType=" + e.getCause().toString());
			throw e;
		}
		return true;
	}

}

package com.soin.sgrm.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.soin.sgrm.security.UserLogin;

public class BaseController {

	public UserLogin getUserLogin() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!auth.getPrincipal().equals("anonymousUser")) {
			UserLogin userLogin = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return userLogin;
		}
		return null;
	}
}

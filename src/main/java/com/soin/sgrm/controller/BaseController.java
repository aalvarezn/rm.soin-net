package com.soin.sgrm.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.security.UserLogin;

public class BaseController {

	public PUser getUserLogin() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!auth.getPrincipal().equals("anonymousUser")) {
			PUser user = (PUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			user.setPassword("****");
			return user;
		}
		return null;
	}
}

package com.soin.sgrm.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.soin.sgrm.model.Authority;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.security.UserLogin;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserInfoService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserInfo user = userService.getUserByUsername(username);
		if (user == null) {
			throw new BadCredentialsException("Usuario o contraseña incorrecto");
		}
		return new UserLogin(user.getId(), user.getUsername(), user.getPassword(), user.getEmailAddress(),
				user.getFullName(), getGrantedAuthorities(user));

	}

	private List<GrantedAuthority> getGrantedAuthorities(UserInfo user) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (Authority authority : user.getAuthorities()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + authority.getName()));
		}
		return authorities;
	}

}

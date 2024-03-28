package com.soin.sgrm.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.soin.sgrm.model.Authority;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.model.pos.PAuthority;
import com.soin.sgrm.model.pos.PUserInfo;
import com.soin.sgrm.security.UserLogin;
import com.soin.sgrm.service.pos.PUserInfoService;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	UserInfoService userService;
	
	@Autowired
	PUserInfoService puserService;
	private final Environment environment;

	@Autowired
	public CustomUserDetailsService(Environment environment) {
		this.environment = environment;
	}

	public String profileActive() {
		String[] activeProfiles = environment.getActiveProfiles();
		for (String profile : activeProfiles) {
			return profile;
		}
		return "";
	}
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		String profile = profileActive();
		if (profile.equals("oracle")) {
			UserInfo user = userService.getUserByUsername(username);
			if (user == null) {
				throw new BadCredentialsException("Usuario o contraseña incorrecto");
			}
			return new UserLogin(user.getId(), user.getUsername(), user.getPassword(), user.getEmailAddress(),
					user.getFullName(), getGrantedAuthorities(user));
		} else if (profile.equals("postgres")) {
			PUserInfo puser = puserService.getUserByUsername(username);
			if (puser == null) {
				throw new BadCredentialsException("Usuario o contraseña incorrecto");
			}
			return new UserLogin(puser.getId(), puser.getUsername(), puser.getPassword(), puser.getEmailAddress(),
					puser.getFullName(), getGrantedAuthoritiesP(puser));
		}
		return null;
		

	}

	private List<GrantedAuthority> getGrantedAuthorities(UserInfo user) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (Authority authority : user.getAuthorities()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + authority.getName()));
		}
		return authorities;
	}
	
	private List<GrantedAuthority> getGrantedAuthoritiesP(PUserInfo user) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (PAuthority pauthority : user.getAuthorities()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + pauthority.getName()));
		}
		return authorities;
	}

}

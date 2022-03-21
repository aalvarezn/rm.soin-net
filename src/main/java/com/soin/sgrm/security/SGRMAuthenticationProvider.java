package com.soin.sgrm.security;

import java.util.Collections;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class SGRMAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();

//		"externaluser".equals(username) && "pass".equals(password)
		if (password.equals("soin12345")) {
			return new UsernamePasswordAuthenticationToken(name, password, Collections.emptyList());
		} else {
			throw new BadCredentialsException("Usuario o Contraseña es incorrecto!");
		}

//		if (shouldAuthenticateAgainstThirdPartySystem()) {
//
//			// use the credentials
//			// and authenticate against the third-party system
//			return new UsernamePasswordAuthenticationToken(name, password, new ArrayList<>());
//		} else {
//			return null;
//		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}

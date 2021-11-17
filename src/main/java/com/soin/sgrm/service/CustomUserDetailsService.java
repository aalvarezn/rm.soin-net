package com.soin.sgrm.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.soin.sgrm.model.Authority;
import com.soin.sgrm.model.UserInfo;

@Service("customUserDetailsService")
public class CustomUserDetailsService  implements UserDetailsService{
	
	@Autowired
	UserInfoService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserInfo user = userService.getUserByUsername(username);
		if(user==null){
            throw new UsernameNotFoundException("Username not found");
        }
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), 
                 true, true, true, true, getGrantedAuthorities(user));
	}
	
	private List<GrantedAuthority> getGrantedAuthorities(UserInfo user){
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for(Authority authority : user.getAuthorities()){
            authorities.add(new SimpleGrantedAuthority("ROLE_"+authority.getName()));
        }
        return authorities;
    }

}

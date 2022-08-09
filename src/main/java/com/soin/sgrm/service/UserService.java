package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.User;
import com.soin.sgrm.model.UserInfo;

public interface UserService {

	boolean isValid(User user);
	
	User getUserByUsername(String username);

	List<User> getUsersRM();
	
}

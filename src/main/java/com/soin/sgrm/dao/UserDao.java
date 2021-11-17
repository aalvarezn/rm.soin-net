package com.soin.sgrm.dao;

import com.soin.sgrm.model.User;


public interface UserDao {
	
	boolean isValid(User user);
	
	User getUserByUsername(String username);

}

package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.User;


public interface UserDao {
	
	boolean isValid(User user);
	
	User getUserByUsername(String username);

	List<User> getUserRM();

	User findUserById(Integer id);

}

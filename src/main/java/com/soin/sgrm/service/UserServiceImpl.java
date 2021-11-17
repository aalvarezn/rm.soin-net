package com.soin.sgrm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.UserDao;
import com.soin.sgrm.model.User;

@Transactional("transactionManager")
@Service("UserService")
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;

	@Override
	public boolean isValid(User user) {
		return userDao.isValid(user);
	}

	@Override
	public User getUserByUsername(String username) {
		// TODO Auto-generated method stub
		return userDao.getUserByUsername(username);
	}

}

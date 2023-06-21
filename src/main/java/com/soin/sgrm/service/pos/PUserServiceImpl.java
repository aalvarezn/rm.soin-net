package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PUserDao;
import com.soin.sgrm.model.pos.PUser;

@Transactional("transactionManagerPos")
@Service("PUserService")
public class PUserServiceImpl implements PUserService {
	
	@Autowired
	private PUserDao userDao;

	@Override
	public boolean isValid(PUser user) {
		return userDao.isValid(user);
	}

	@Override
	public PUser getUserByUsername(String username) {
	
		return userDao.getUserByUsername(username);
	}
	
	@Override
	public List<PUser> getUsersRM() {
	
		return userDao.getUserRM();
	}
	@Override
	public PUser findUserById(Integer id) {
	
		return userDao.findUserById(id);
	}
	


}

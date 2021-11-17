package com.soin.sgrm.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.UserInfoDao;
import com.soin.sgrm.model.UserInfo;

@Transactional("transactionManager")
@Service("UserInfoService")
public class UserInfoServiceImpl implements UserInfoService {

	@Autowired
	private UserInfoDao dao;

	@Override
	public List<UserInfo> list() {
		return dao.list();
	}

	@Override
	public UserInfo getUserByUsername(String username) {
		return dao.getUserByUsername(username);
	}

	@Override
	public void changePassword(UserInfo userInfo) throws SQLException {
		dao.changePassword(userInfo);
	}

	@Override
	public UserInfo findUserInfoById(Integer id) {
		return dao.findUserInfoById(id);
	}

	@Override
	public void softDelete(UserInfo userInfo) {
		dao.softDelete(userInfo);
	}

	@Override
	public void updateUserInfo(UserInfo userInfo) {
		dao.updateUserInfo(userInfo);
	}

	@Override
	public boolean uniqueUsername(UserInfo userInfo) {
		return dao.uniqueUsername(userInfo);
	}

	@Override
	public UserInfo getUserByEmail(String email) {
		return dao.getUserByEmail(email);
	}

	@Override
	public void saveUserInfo(UserInfo userInfo) {
		dao.saveUserInfo(userInfo);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

}

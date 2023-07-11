package com.soin.sgrm.service.pos;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.UserInfoDao;
import com.soin.sgrm.dao.pos.PUserInfoDao;
import com.soin.sgrm.model.User;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.model.pos.PUserInfo;

@Transactional("transactionManagerPos")
@Service("PUserInfoService")
public class PUserInfoServiceImpl implements PUserInfoService {

	@Autowired
	private PUserInfoDao dao;

	@Override
	public List<PUserInfo> list() {
		return dao.list();
	}

	@Override
	public PUserInfo getUserByUsername(String username) {
		return dao.getUserByUsername(username);
	}

	@Override
	public void changePassword(PUserInfo userInfo) throws SQLException {
		dao.changePassword(userInfo);
	}

	@Override
	public PUserInfo findUserInfoById(Integer id) {
		return dao.findUserInfoById(id);
	}

	@Override
	public void softDelete(PUserInfo userInfo) {
		dao.softDelete(userInfo);
	}

	@Override
	public void updateUserInfo(PUserInfo userInfo) {
		dao.updateUserInfo(userInfo);
	}

	@Override
	public boolean uniqueUsername(PUserInfo userInfo) {
		return dao.uniqueUsername(userInfo);
	}

	@Override
	public PUserInfo getUserByEmail(String email) {
		return dao.getUserByEmail(email);
	}

	@Override
	public void saveUserInfo(PUserInfo userInfo) {
		dao.saveUserInfo(userInfo);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

	@Override
	public PUser findUserById(Integer id) {
		return dao.findUserById(id);
	}

	@Override
	public PUserInfo getUserByGitUsername(String username) {
		return dao.getUserByGitUsername(username);
	}

	@Override
	public boolean uniqueGitUsername(PUserInfo userInfo) {
		return dao.uniqueGitUsername(userInfo);
	}

}

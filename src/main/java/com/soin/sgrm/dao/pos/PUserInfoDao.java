package com.soin.sgrm.dao.pos;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.User;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.model.pos.PUserInfo;

public interface PUserInfoDao {

//	UserInfo findByUsername(String username);

	List<PUserInfo> list();

	PUserInfo getUserByUsername(String username);

	PUserInfo getUserByEmail(String email);

	PUserInfo findUserInfoById(Integer id);

	void changePassword(PUserInfo userInfo) throws SQLException;

	void softDelete(PUserInfo userInfo);

	void delete(Integer id);

	void updateUserInfo(PUserInfo userInfo);

	void saveUserInfo(PUserInfo userInfo);

	boolean uniqueUsername(PUserInfo userInfo);
	
	PUser findUserById(Integer id);

	PUserInfo getUserByGitUsername(String username);

	boolean uniqueGitUsername(PUserInfo userInfo);

}

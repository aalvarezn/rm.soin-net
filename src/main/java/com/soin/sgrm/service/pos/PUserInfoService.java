package com.soin.sgrm.service.pos;

import java.sql.SQLException;
import java.util.List;

import com.soin.sgrm.model.User;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.model.pos.PUserInfo;

public interface PUserInfoService {

	List<PUserInfo> list();

	PUserInfo getUserByUsername(String username);
	
	PUserInfo getUserByGitUsername(String username);

	PUserInfo getUserByEmail(String email);

	PUserInfo findUserInfoById(Integer id);

	void changePassword(PUserInfo userInfo) throws SQLException;

	void softDelete(PUserInfo userInfo);

	void delete(Integer id);

	void updateUserInfo(PUserInfo userInfo);

	void saveUserInfo(PUserInfo userInfo);

	boolean uniqueUsername(PUserInfo userInfo);
	
	PUser findUserById(Integer id);

	boolean uniqueGitUsername(PUserInfo userInfo);
}

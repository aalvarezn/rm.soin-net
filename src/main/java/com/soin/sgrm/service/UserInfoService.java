package com.soin.sgrm.service;

import java.sql.SQLException;
import java.util.List;

import com.soin.sgrm.model.UserInfo;

public interface UserInfoService {

	List<UserInfo> list();

	UserInfo getUserByUsername(String username);

	UserInfo getUserByEmail(String email);

	UserInfo findUserInfoById(Integer id);

	void changePassword(UserInfo userInfo) throws SQLException;

	void softDelete(UserInfo userInfo);

	void delete(Integer id);

	void updateUserInfo(UserInfo userInfo);

	void saveUserInfo(UserInfo userInfo);

	boolean uniqueUsername(UserInfo userInfo);
}

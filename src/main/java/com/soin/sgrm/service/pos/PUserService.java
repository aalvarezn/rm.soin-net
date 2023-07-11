package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.User;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.model.pos.PUser;

public interface PUserService {

	boolean isValid(PUser user);
	
	PUser getUserByUsername(String username);

	List<PUser> getUsersRM();

	PUser findUserById(Integer id);
	
}

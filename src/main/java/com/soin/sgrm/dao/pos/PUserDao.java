package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.User;
import com.soin.sgrm.model.pos.PUser;


public interface PUserDao {
	
	boolean isValid(PUser user);
	
	PUser getUserByUsername(String username);

	List<PUser> getUserRM();

	PUser findUserById(Integer id);

}

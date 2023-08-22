package com.soin.sgrm.dao.pos.wf;

import java.util.List;

import com.soin.sgrm.model.pos.wf.PWFUser;


public interface PWFUserDao {

	List<PWFUser> list();
	
	PWFUser findWFUserById(Integer id);

}

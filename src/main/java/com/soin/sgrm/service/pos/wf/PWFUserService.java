package com.soin.sgrm.service.pos.wf;

import java.util.List;

import com.soin.sgrm.model.pos.wf.PWFUser;

public interface PWFUserService {

	List<PWFUser> list();
	
	PWFUser findWFUserById(Integer id);
}

package com.soin.sgrm.service.wf;

import java.util.List;

import com.soin.sgrm.model.wf.WFUser;

public interface WFUserService {

	List<WFUser> list();
	
	WFUser findWFUserById(Integer id);
}

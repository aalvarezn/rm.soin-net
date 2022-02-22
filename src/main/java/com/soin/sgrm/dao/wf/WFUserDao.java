package com.soin.sgrm.dao.wf;

import java.util.List;

import com.soin.sgrm.model.wf.WFUser;

public interface WFUserDao {

	List<WFUser> list();
	
	WFUser findWFUserById(Integer id);

}

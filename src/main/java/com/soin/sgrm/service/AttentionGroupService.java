package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.AttentionGroup;

public interface AttentionGroupService extends BaseService<Long, AttentionGroup>{

	 List<AttentionGroup> findGroupByUserId(Integer id);
	
}

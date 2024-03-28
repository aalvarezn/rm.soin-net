package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PAttentionGroup;

public interface PAttentionGroupService extends BaseService<Long, PAttentionGroup>{

	 List<PAttentionGroup> findGroupByUserId(Integer id);
	
}

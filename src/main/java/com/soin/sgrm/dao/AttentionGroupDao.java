package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.AttentionGroup;

public interface AttentionGroupDao extends BaseDao<Long, AttentionGroup> {

	 List<AttentionGroup> findGroupByUserId(Integer id);

}

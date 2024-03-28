package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.AttentionGroup;
import com.soin.sgrm.model.pos.PAttentionGroup;

public interface PAttentionGroupDao extends BaseDao<Long, PAttentionGroup> {

	 List<PAttentionGroup> findGroupByUserId(Integer id);

}

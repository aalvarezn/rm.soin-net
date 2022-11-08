package com.soin.sgrm.dao;

import com.soin.sgrm.model.BaseKnowledge;

public interface BaseKnowledgeDao extends BaseDao<Long, BaseKnowledge>{
	public Integer existNumError(String number_release);
	
	void updateStatusBaseKnowledge(BaseKnowledge BaseKnowledge, String dateChange) throws Exception;

	public Integer countByType(Integer id, String type, int query, Object[] ids);

	Integer countByManager(Integer id, Long idBaseKnowledge);
}

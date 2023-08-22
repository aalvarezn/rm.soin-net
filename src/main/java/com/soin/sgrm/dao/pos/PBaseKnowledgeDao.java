package com.soin.sgrm.dao.pos;

import com.soin.sgrm.model.pos.PBaseKnowledge;

public interface PBaseKnowledgeDao extends BaseDao<Long, PBaseKnowledge>{
	public Integer existNumError(String number_release);
	
	void updateStatusBaseKnowledge(PBaseKnowledge BaseKnowledge, String dateChange) throws Exception;

	public Integer countByType(Integer id, String type, int query, Object[] ids);

	Integer countByManager(Integer id, Long idBaseKnowledge);
}

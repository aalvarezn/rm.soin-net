package com.soin.sgrm.dao;

import com.soin.sgrm.model.RFC;

public interface RFCDao extends BaseDao<Long, RFC>{
	public Integer existNumRelease(String number_release);
	
	void updateStatusRFC(RFC rfc, String dateChange) throws Exception;

	public Integer countByType(Integer id, String type, int query, Object[] ids);
}

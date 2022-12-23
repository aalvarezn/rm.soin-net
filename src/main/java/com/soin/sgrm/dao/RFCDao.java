package com.soin.sgrm.dao;

import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.RFC_WithoutRelease;

public interface RFCDao extends BaseDao<Long, RFC>{
	public Integer existNumRFC(String number_release);
	
	void updateStatusRFC(RFC rfc, String dateChange) throws Exception;

	public Integer countByType(Integer id, String type, int query, Object[] ids);

	Integer countByManager(Integer id, Long idRFC);

	public RFC_WithoutRelease findRfcWithRelease(Long id);
}

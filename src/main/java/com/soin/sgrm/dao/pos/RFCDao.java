package com.soin.sgrm.dao.pos;

import com.soin.sgrm.model.pos.PRFC;

public interface RFCDao extends BaseDao<Long, PRFC> {
	public Integer existNumRelease(String number_release);



	void updateStatusRFC(PRFC rfc, String dateChange) throws Exception;
}

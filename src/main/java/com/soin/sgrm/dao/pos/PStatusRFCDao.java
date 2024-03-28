package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PStatusRFC;

public interface PStatusRFCDao extends BaseDao<Long, PStatusRFC> {

	List<PStatusRFC> findWithFilter();

}

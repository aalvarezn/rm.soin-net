package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.StatusRFC;

public interface StatusRFCDao extends BaseDao<Long, StatusRFC> {

	List<StatusRFC> findWithFilter();

}

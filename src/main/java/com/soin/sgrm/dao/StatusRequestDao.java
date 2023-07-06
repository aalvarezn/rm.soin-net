package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.StatusRequest;

public interface StatusRequestDao extends BaseDao<Long, StatusRequest> {

	List<StatusRequest> findWithFilter();

}

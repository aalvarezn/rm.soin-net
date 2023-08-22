package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PStatusRequest;

public interface PStatusRequestDao extends BaseDao<Long, PStatusRequest> {

	List<PStatusRequest> findWithFilter();

}

package com.soin.sgrm.dao;

import com.soin.sgrm.model.Request_Estimate;

public interface Request_EstimateDao extends BaseDao<Long, Request_Estimate> {

	Request_Estimate findByIdRequest(Long idRequest);


}

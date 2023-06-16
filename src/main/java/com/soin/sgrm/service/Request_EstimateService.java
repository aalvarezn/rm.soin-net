package com.soin.sgrm.service;

import com.soin.sgrm.model.Request_Estimate;

public interface Request_EstimateService extends BaseService<Long, Request_Estimate>{

	Request_Estimate findByIdRequest(Long idRequest);

}

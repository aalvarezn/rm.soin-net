package com.soin.sgrm.service.pos;


import com.soin.sgrm.model.pos.PRequest_Estimate;

public interface PRequest_EstimateService extends BaseService<Long, PRequest_Estimate>{

	PRequest_Estimate findByIdRequest(Long idRequest);

}

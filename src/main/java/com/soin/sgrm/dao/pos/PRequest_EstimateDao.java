package com.soin.sgrm.dao.pos;


import com.soin.sgrm.model.pos.PRequest_Estimate;

public interface PRequest_EstimateDao extends BaseDao<Long, PRequest_Estimate> {

	PRequest_Estimate findByIdRequest(Long idRequest);


}

package com.soin.sgrm.dao.pos;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PRequest_Estimate;

@Repository
public class PRequest_EstimateDaoImpl extends  AbstractDao<Long, PRequest_Estimate> implements PRequest_EstimateDao{

	@Override
	public PRequest_Estimate findByIdRequest(Long idRequest) {
		  return (PRequest_Estimate) getSession().createCriteria(PRequest_Estimate.class)
		    		.createAlias("requestBase","requestBase")
		    		.add(Restrictions.eq("requestBase.id", idRequest))
		    		.uniqueResult();
		
	}

}

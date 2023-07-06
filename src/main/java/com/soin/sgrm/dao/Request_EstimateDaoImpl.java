package com.soin.sgrm.dao;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Request_Estimate;

@Repository
public class Request_EstimateDaoImpl extends  AbstractDao<Long, Request_Estimate> implements Request_EstimateDao{

	@Override
	public Request_Estimate findByIdRequest(Long idRequest) {
		  return (Request_Estimate) getSession().createCriteria(Request_Estimate.class)
		    		.createAlias("requestBase","requestBase")
		    		.add(Restrictions.eq("requestBase.id", idRequest))
		    		.uniqueResult();
		
	}

}

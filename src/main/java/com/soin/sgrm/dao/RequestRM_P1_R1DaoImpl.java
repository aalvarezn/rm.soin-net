package com.soin.sgrm.dao;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.RequestRM_P1_R1;

@Repository
public class RequestRM_P1_R1DaoImpl extends AbstractDao<Long, RequestRM_P1_R1> implements RequestRM_P1_R1Dao{

	@Override
	public RequestRM_P1_R1 requestRM1(Long id){
    return (RequestRM_P1_R1) getSession().createCriteria(RequestRM_P1_R1.class)
    		.createAlias("requestBase","requestBase")
    		.add(Restrictions.eq("requestBase.id", id))
    		.uniqueResult();
	}
	
}

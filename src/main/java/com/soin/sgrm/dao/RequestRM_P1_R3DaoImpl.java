package com.soin.sgrm.dao;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.RequestRM_P1_R3;

@Repository
public class RequestRM_P1_R3DaoImpl extends AbstractDao<Long, RequestRM_P1_R3> implements RequestRM_P1_R3Dao{

	@Override
	public RequestRM_P1_R3 requestRm3(Long id){
    return (RequestRM_P1_R3) getSession().createCriteria(RequestRM_P1_R3.class)
    		.createAlias("requestBase","requestBase")
    		.add(Restrictions.eq("requestBase.id", id))
    		.uniqueResult();
	}
	
}

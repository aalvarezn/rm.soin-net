package com.soin.sgrm.dao;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.RequestRM_P1_R5;

@Repository
public class RequestRM_P1_R5DaoImpl extends AbstractDao<Long, RequestRM_P1_R5> implements RequestRM_P1_R5Dao{

	@Override
	public RequestRM_P1_R5 requestRM5(Long id){
    return (RequestRM_P1_R5) getSession().createCriteria(RequestRM_P1_R5.class)
    		.createAlias("requestBase","requestBase")
    		.add(Restrictions.eq("requestBase.id", id))
    		.uniqueResult();
	}
	
}

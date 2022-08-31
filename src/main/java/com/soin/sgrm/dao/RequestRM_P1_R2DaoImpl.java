package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.RequestRM_P1_R2;
import com.soin.sgrm.model.RequestRM_P1_R4;
import com.soin.sgrm.model.RequestRM_P1_R5;

@Repository
public class RequestRM_P1_R2DaoImpl extends AbstractDao<Long, RequestRM_P1_R2> implements RequestRM_P1_R2Dao{

	@Override
	@SuppressWarnings("unchecked")
	public RequestRM_P1_R2 requestRM2(Long id){
    return (RequestRM_P1_R2) getSession().createCriteria(RequestRM_P1_R2.class)
    		.createAlias("requestBase","requestBase")
    		.add(Restrictions.eq("requestBase.id", id))
    		.uniqueResult();
	}
	
}

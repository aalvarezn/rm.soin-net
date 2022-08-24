package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.RequestRM_P1_R4;

@Repository
public class RequestRM_P1_R4DaoImpl extends AbstractDao<Long, RequestRM_P1_R4> implements RequestRM_P1_R4Dao{

	@Override
	@SuppressWarnings("unchecked")
	public List<RequestRM_P1_R4> listRequestRm4(Long id){
    return getSession().createCriteria(RequestRM_P1_R4.class)
    		.createAlias("requestBase","requestBase")
    		.add(Restrictions.eq("requestBase.id", id))
    		.list();
	}
	
}

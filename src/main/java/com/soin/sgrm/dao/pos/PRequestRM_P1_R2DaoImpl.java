package com.soin.sgrm.dao.pos;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;


import com.soin.sgrm.model.pos.PRequestRM_P1_R2;

@Repository
public class PRequestRM_P1_R2DaoImpl extends AbstractDao<Long, PRequestRM_P1_R2> implements PRequestRM_P1_R2Dao{

	@Override
	@SuppressWarnings("unchecked")
	public PRequestRM_P1_R2 requestRM2(Long id){
    return (PRequestRM_P1_R2) getSession().createCriteria(PRequestRM_P1_R2.class)
    		.createAlias("requestBase","requestBase")
    		.add(Restrictions.eq("requestBase.id", id))
    		.uniqueResult();
	}
	
}

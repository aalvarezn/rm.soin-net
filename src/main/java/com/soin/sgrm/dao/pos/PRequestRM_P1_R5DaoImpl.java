package com.soin.sgrm.dao.pos;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PRequestRM_P1_R5;

@Repository
public class PRequestRM_P1_R5DaoImpl extends AbstractDao<Long, PRequestRM_P1_R5> implements PRequestRM_P1_R5Dao{

	@Override
	public PRequestRM_P1_R5 requestRM5(Long id){
    return (PRequestRM_P1_R5) getSession().createCriteria(PRequestRM_P1_R5.class)
    		.createAlias("requestBase","requestBase")
    		.add(Restrictions.eq("requestBase.id", id))
    		.uniqueResult();
	}
	
}

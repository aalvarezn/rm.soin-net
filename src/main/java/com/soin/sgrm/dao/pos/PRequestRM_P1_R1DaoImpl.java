package com.soin.sgrm.dao.pos;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PRequestRM_P1_R1;

@Repository
public class PRequestRM_P1_R1DaoImpl extends AbstractDao<Long, PRequestRM_P1_R1> implements PRequestRM_P1_R1Dao{

	@Override
	public PRequestRM_P1_R1 requestRM1(Long id){
    return (PRequestRM_P1_R1) getSession().createCriteria(PRequestRM_P1_R1.class)
    		.createAlias("requestBase","requestBase")
    		.add(Restrictions.eq("requestBase.id", id))
    		.uniqueResult();
	}
	
}

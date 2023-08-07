package com.soin.sgrm.dao.pos;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PRequestRM_P1_R3;



@Repository
public class PRequestRM_P1_R3DaoImpl extends AbstractDao<Long, PRequestRM_P1_R3> implements PRequestRM_P1_R3Dao{

	@Override
	public PRequestRM_P1_R3 requestRm3(Long id){
    return (PRequestRM_P1_R3) getSession().createCriteria(PRequestRM_P1_R3.class)
    		.createAlias("requestBase","requestBase")
    		.add(Restrictions.eq("requestBase.id", id))
    		.uniqueResult();
	}
	
}

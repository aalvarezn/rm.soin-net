package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PRequestRM_P1_R4;



@Repository
public class PRequestRM_P1_R4DaoImpl extends AbstractDao<Long, PRequestRM_P1_R4> implements PRequestRM_P1_R4Dao{

	@Override
	@SuppressWarnings("unchecked")
	public List<PRequestRM_P1_R4> listRequestRm4(Long id){
    return getSession().createCriteria(PRequestRM_P1_R4.class)
    		.createAlias("requestBase","requestBase")
    		.add(Restrictions.eq("requestBase.id", id))
    		.list();
	}
	
}

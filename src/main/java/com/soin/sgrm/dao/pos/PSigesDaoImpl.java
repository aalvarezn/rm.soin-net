package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PSiges;

@Repository
public class PSigesDaoImpl extends  AbstractDao<Long, PSiges> implements PSigesDao{

	@SuppressWarnings("unchecked")
	public List<PSiges> listCodeSiges(Integer id){
    return getSession().createCriteria(PSiges.class)
    		.createAlias("system","systems")
    		.add(Restrictions.eq("systems.id", id))
    		.list();
	}

	@Override
	public boolean checkUniqueCode(String codeSiges) {
		Criteria crit = getSession().createCriteria(PSiges.class);
		crit.add(Restrictions.eq("codeSiges", codeSiges));
		crit.setProjection(Projections.rowCount());
	    Long count = (Long) crit.uniqueResult();
	    return count == 0;
	}



}

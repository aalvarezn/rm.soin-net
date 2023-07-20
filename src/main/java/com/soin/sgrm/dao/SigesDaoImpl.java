package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Siges;

@Repository
public class SigesDaoImpl extends  AbstractDao<Long, Siges> implements SigesDao{

	@SuppressWarnings("unchecked")
	public List<Siges> listCodeSiges(Integer id){
    return getSession().createCriteria(Siges.class)
    		.createAlias("system","systems")
    		.add(Restrictions.eq("systems.id", id))
    		.list();
	}

	@Override
	public boolean checkUniqueCode(String sigesCode) {
		Criteria crit = getSession().createCriteria(Siges.class);
		crit.add(Restrictions.eq("codeSiges", sigesCode));
		crit.setProjection(Projections.rowCount());
	    Long count = (Long) crit.uniqueResult();
	    return count == 0;
	}

	@Override
	public boolean veryUpdateSigesCode(Long id, String codeSiges) {
		Criteria crit = getSession().createCriteria(Siges.class);
		crit.add(Restrictions.eq("codeSiges", codeSiges));
		crit.add(Restrictions.eq("id", id));
		crit.setProjection(Projections.rowCount());
	    Long count = (Long) crit.uniqueResult();
	    return count == 1;
	}

	@Override
	public boolean veryUpdateSigesCodeDif(Long id, String codeSiges) {
		Criteria crit = getSession().createCriteria(Siges.class);
		crit.add(Restrictions.eq("codeSiges", codeSiges));
		crit.add(Restrictions.ne("id", id));
		crit.setProjection(Projections.rowCount());
	    Long count = (Long) crit.uniqueResult();
	    return count > 0;
	}



}
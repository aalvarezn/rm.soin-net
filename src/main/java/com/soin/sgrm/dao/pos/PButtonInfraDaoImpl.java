package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PButtonInfra;

@Repository
public class PButtonInfraDaoImpl extends  AbstractDao<Long, PButtonInfra> implements PButtonInfraDao{

	@SuppressWarnings("unchecked")
	public List<PButtonInfra> listCodeSiges(Integer id){
    return getSession().createCriteria(PButtonInfra.class)
    		.createAlias("system","systems")
    		.add(Restrictions.eq("systems.id", id))
    		.list();
	}

	@Override
	public boolean checkUniqueCode(String codeSiges) {
		Criteria crit = getSession().createCriteria(PButtonInfra.class);
		crit.add(Restrictions.eq("codeSiges", codeSiges));
		crit.setProjection(Projections.rowCount());
	    Long count = (Long) crit.uniqueResult();
	    return count == 0;
	}
	@Override
	public boolean veryUpdateSigesCode(Long id, String codeSiges) {
		Criteria crit = getSession().createCriteria(PButtonInfra.class);
		crit.add(Restrictions.eq("codeSiges", codeSiges));
		crit.add(Restrictions.eq("id", id));
		crit.setProjection(Projections.rowCount());
	    Long count = (Long) crit.uniqueResult();
	    return count == 1;
	}

	@Override
	public boolean veryUpdateSigesCodeDif(Long id, String codeSiges) {
		Criteria crit = getSession().createCriteria(PButtonInfra.class);
		crit.add(Restrictions.eq("codeSiges", codeSiges));
		crit.add(Restrictions.ne("id", id));
		crit.setProjection(Projections.rowCount());
	    Long count = (Long) crit.uniqueResult();
	    return count > 0;
	}


}
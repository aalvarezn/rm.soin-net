package com.soin.sgrm.dao.pos;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PRFC;

@Repository("rfcDao")
public class RFCDaoImpl extends AbstractDao<Long, PRFC> implements RFCDao {
	
	
	
	@Override
	public Integer existNumRelease(String numRequest) {

		Criteria crit = getSession().createCriteria(PRFC.class);
		crit.add(Restrictions.like("numRequest", numRequest, MatchMode.ANYWHERE));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		int recordsTotal = count.intValue();

		return recordsTotal;

	}

}

package com.soin.sgrm.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.RequestBase;

@Repository
public class RequestBaseDaoImpl extends AbstractDao<Long, RequestBase> implements RequestBaseDao{
	@Autowired
	private SessionFactory sessionFactory;
	
	
	@Override
	public Integer existNumRequest(String numRequest) {
		Criteria crit = getSession().createCriteria(RequestBase.class);
		crit.add(Restrictions.like("numRequest", numRequest, MatchMode.ANYWHERE));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		int recordsTotal = count.intValue();
		return recordsTotal;

	}
}

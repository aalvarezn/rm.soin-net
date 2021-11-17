package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Impact;

@Repository
public class ImpactDaoImpl implements ImpactDao {

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Impact> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Impact.class);
		List<Impact> list = crit.list();
		return list;
	}

	@Override
	public Impact findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Impact.class);
		crit.add(Restrictions.eq("id", id));
		return (Impact) crit.uniqueResult();
	}

	@Override
	public void save(Impact impact) {
		sessionFactory.getCurrentSession().save(impact);
	}

	@Override
	public void update(Impact impact) {
		sessionFactory.getCurrentSession().update(impact);
	}

	@Override
	public void delete(Integer id) {
		Impact impact = findById(id);
		sessionFactory.getCurrentSession().delete(impact);
	}

}

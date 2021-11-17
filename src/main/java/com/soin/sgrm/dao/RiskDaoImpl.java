package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Risk;

@Repository
public class RiskDaoImpl implements RiskDao {

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Risk> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Risk.class);
		return crit.list();
	}

	@Override
	public Risk findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Risk.class);
		crit.add(Restrictions.eq("id", id));
		return (Risk) crit.uniqueResult();
	}

	@Override
	public void save(Risk risk) {
		sessionFactory.getCurrentSession().save(risk);
	}

	@Override
	public void update(Risk risk) {
		sessionFactory.getCurrentSession().update(risk);
	}

	@Override
	public void delete(Integer id) {
		Risk risk = findById(id);
		sessionFactory.getCurrentSession().delete(risk);
	}

}

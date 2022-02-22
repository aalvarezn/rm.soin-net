package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Environment;

@Repository
public class EnvironmentDaoImpl implements EnvironmentDao {

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Environment> listBySystem(Integer id) {
		List<Environment> list = sessionFactory.getCurrentSession().createCriteria(Environment.class)
				.add(Restrictions.eq("system.id", id)).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Environment> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Environment.class);
		return crit.list();
	}

	@Override
	public Environment findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Environment.class);
		crit.add(Restrictions.eq("id", id));
		return (Environment) crit.uniqueResult();
	}

	@Override
	public void save(Environment environment) {
		sessionFactory.getCurrentSession().save(environment);
	}

	@Override
	public void update(Environment environment) {
		sessionFactory.getCurrentSession().update(environment);
	}

	@Override
	public void delete(Integer id) {
		Environment environment = findById(id);
		sessionFactory.getCurrentSession().delete(environment);
	}

}

package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Priority;

@Repository
public class PriorityDaoImpl implements PriorityDao {

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Priority> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Priority.class);
		return crit.list();
	}

	@Override
	public Priority findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Priority.class);
		crit.add(Restrictions.eq("id", id));
		return (Priority) crit.uniqueResult();
	}

	@Override
	public void save(Priority priority) {
		sessionFactory.getCurrentSession().save(priority);
	}

	@Override
	public void update(Priority priority) {
		sessionFactory.getCurrentSession().update(priority);
	}

	@Override
	public void delete(Integer id) {
		Priority priority = findById(id);
		sessionFactory.getCurrentSession().delete(priority);
	}

}

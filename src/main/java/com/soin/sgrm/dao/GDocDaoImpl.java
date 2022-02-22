package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.GDoc;

@Repository
public class GDocDaoImpl implements GDocDao {

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<GDoc> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(GDoc.class);
		return crit.list();
	}

	@Override
	public GDoc findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(GDoc.class);
		crit.add(Restrictions.eq("id", id));
		return (GDoc) crit.uniqueResult();
	}

	@Override
	public void save(GDoc gDoc) {
		sessionFactory.getCurrentSession().save(gDoc);
	}

	@Override
	public void update(GDoc gDoc) {
		sessionFactory.getCurrentSession().update(gDoc);
	}

	@Override
	public void delete(Integer id) {
		GDoc gDoc = findById(id);
		sessionFactory.getCurrentSession().delete(gDoc);
	}

}

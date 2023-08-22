package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.TypeAmbient;
import com.soin.sgrm.model.pos.PTypeAmbient;

@Repository
public class PTypeAmbientDaoImpl implements PTypeAmbientDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PTypeAmbient> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PTypeAmbient.class);
		return crit.list();
	}

	@Override
	public PTypeAmbient findByName(String name) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PTypeAmbient.class);
		crit.add(Restrictions.eq("name", name));
		PTypeAmbient typeambient = (PTypeAmbient) crit.uniqueResult();
		return typeambient;
	}

	@Override
	public PTypeAmbient findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PTypeAmbient.class);
		crit.add(Restrictions.eq("id", id));
		return (PTypeAmbient) crit.uniqueResult();
	}

	@Override
	public void save(PTypeAmbient typeambient) {
		sessionFactory.getCurrentSession().save(typeambient);
	}

	@Override
	public void update(PTypeAmbient typeambient) {
		sessionFactory.getCurrentSession().update(typeambient);
	}

	@Override
	public void delete(Integer id) {
		PTypeAmbient typeambient = findById(id);
		sessionFactory.getCurrentSession().delete(typeambient);
	}

}

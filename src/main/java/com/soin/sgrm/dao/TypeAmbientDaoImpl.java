package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.TypeAmbient;

@Repository
public class TypeAmbientDaoImpl implements TypeAmbientDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<TypeAmbient> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(TypeAmbient.class);
		return crit.list();
	}

	@Override
	public TypeAmbient findByName(String name) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(TypeAmbient.class);
		crit.add(Restrictions.eq("name", name));
		TypeAmbient typeambient = (TypeAmbient) crit.uniqueResult();
		return typeambient;
	}

	@Override
	public TypeAmbient findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(TypeAmbient.class);
		crit.add(Restrictions.eq("id", id));
		return (TypeAmbient) crit.uniqueResult();
	}

	@Override
	public void save(TypeAmbient typeambient) {
		sessionFactory.getCurrentSession().save(typeambient);
	}

	@Override
	public void update(TypeAmbient typeambient) {
		sessionFactory.getCurrentSession().update(typeambient);
	}

	@Override
	public void delete(Integer id) {
		TypeAmbient typeambient = findById(id);
		sessionFactory.getCurrentSession().delete(typeambient);
	}

}

package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Component;


@Repository
public class ComponentDaoImpl extends AbstractDao<Long, Component> implements ComponentDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Component> findBySystem(List<Integer> systemIds) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Component.class);
		crit.createAlias("system", "system");
		crit.add( Restrictions.in("system.id", systemIds));
		List<Component> componentList = crit.list();
		return componentList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Component> findBySystem(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Component.class);
		crit.createAlias("system", "system");
		crit.add( Restrictions.eq("system.id", id));
		List<Component> componentList = crit.list();
		return componentList;
	}

}

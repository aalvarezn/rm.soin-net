package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Component;
import com.soin.sgrm.model.pos.PComponent;


@Repository
public class PComponentDaoImpl extends AbstractDao<Long, PComponent> implements PComponentDao{

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PComponent> findBySystem(List<Integer> systemIds) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PComponent.class);
		crit.createAlias("system", "system");
		crit.add( Restrictions.in("system.id", systemIds));
		List<PComponent> componentList = crit.list();
		return componentList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PComponent> findBySystem(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PComponent.class);
		crit.createAlias("system", "system");
		crit.add( Restrictions.eq("system.id", id));
		List<PComponent> componentList = crit.list();
		return componentList;
	}

}

package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.ConfigurationItem;
import com.soin.sgrm.model.pos.PConfigurationItem;

@Repository
public class PConfigurationItemDaoImpl implements PConfigurationItemDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PConfigurationItem> listBySystem(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PConfigurationItem.class);
		crit.add(Restrictions.eq("system.id", id));
		List<PConfigurationItem> list = crit.list();
		return list;
	}

	@Override
	public PConfigurationItem findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PConfigurationItem.class);
		crit.add(Restrictions.eq("id", id));
		PConfigurationItem configurationItem = (PConfigurationItem) crit.uniqueResult();
		return configurationItem;
	}

	@Override
	public boolean existItem(String name, Integer system_id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PConfigurationItem.class);
		crit.add(Restrictions.eq("name", name));
		crit.add(Restrictions.eq("system.id", system_id));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return (count > 0);
	}

	@Override
	public PConfigurationItem findByName(String name, Integer system_id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PConfigurationItem.class);
		crit.add(Restrictions.eq("name", name));
		crit.add(Restrictions.eq("system.id", system_id));
		crit.setMaxResults(1);
		PConfigurationItem item = (PConfigurationItem) crit.uniqueResult();
		return item;
	}

	@Override
	public void save(PConfigurationItem configurationItem) {
		sessionFactory.getCurrentSession().save(configurationItem);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PConfigurationItem> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PConfigurationItem.class);
		return crit.list();
	}

	@Override
	public void update(PConfigurationItem configurationItem) {
		sessionFactory.getCurrentSession().update(configurationItem);
	}

	@Override
	public void delete(Integer id) {
		PConfigurationItem configurationItem = findById(id);
		sessionFactory.getCurrentSession().delete(configurationItem);
	}

}

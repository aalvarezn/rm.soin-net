package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.ConfigurationItem;

@Repository
public class ConfigurationItemDaoImpl implements ConfigurationItemDao {

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigurationItem> listBySystem(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ConfigurationItem.class);
		crit.add(Restrictions.eq("system_id", id));
		List<ConfigurationItem> list = crit.list();
		return list;
	}

	@Override
	public ConfigurationItem findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ConfigurationItem.class);
		crit.add(Restrictions.eq("id", id));
		ConfigurationItem configurationItem = (ConfigurationItem) crit.uniqueResult();
		return configurationItem;
	}

	@Override
	public boolean existItem(String name, Integer system_id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ConfigurationItem.class);
		crit.add(Restrictions.eq("name", name));
		crit.add(Restrictions.eq("system_id", system_id));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return (count > 0);
	}

	@Override
	public ConfigurationItem findByName(String name, Integer system_id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ConfigurationItem.class);
		crit.add(Restrictions.eq("name", name));
		crit.add(Restrictions.eq("system_id", system_id));
		crit.setMaxResults(1);
		ConfigurationItem item = (ConfigurationItem) crit.uniqueResult();
		return item;
	}

	@Override
	public ConfigurationItem save(ConfigurationItem confItem) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.saveOrUpdate(confItem);
			transObj.commit();
			return confItem;
		} catch (Exception e) {
			e.printStackTrace();
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

}

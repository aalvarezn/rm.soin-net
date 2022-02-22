package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.SystemConfiguration;

@Repository
public class SystemConfigurationDaoImpl implements SystemConfigurationDao {
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public SystemConfiguration findBySystemId(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(SystemConfiguration.class);
		crit.add(Restrictions.eq("system.id", id));
		SystemConfiguration systemConf = (SystemConfiguration) crit.uniqueResult();
		return systemConf;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<SystemConfiguration> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(SystemConfiguration.class);
		List<SystemConfiguration> list = crit.list();
		return list;
	}

	@Override
	public SystemConfiguration findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(SystemConfiguration.class);
		crit.add(Restrictions.eq("id", id));
		SystemConfiguration systemConf = (SystemConfiguration) crit.uniqueResult();
		return systemConf;
	}

	@Override
	public SystemConfiguration update(SystemConfiguration systemConfig) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.update(systemConfig);
			transObj.commit();
			return systemConfig;
		} catch (Exception e) {
			Sentry.capture(e, "configuration");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public void save(SystemConfiguration systemConfig) {
		sessionFactory.getCurrentSession().save(systemConfig);
	}
}

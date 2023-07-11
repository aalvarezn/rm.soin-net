package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.pos.PSystemConfiguration;

@Repository
public class PSystemConfigurationDaoImpl implements PSystemConfigurationDao {
	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;
;

	@Override
	public PSystemConfiguration findBySystemId(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PSystemConfiguration.class);
		crit.add(Restrictions.eq("system.id", id));
		PSystemConfiguration systemConf = (PSystemConfiguration) crit.uniqueResult();
		return systemConf;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<PSystemConfiguration> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PSystemConfiguration.class);
		List<PSystemConfiguration> list = crit.list();
		return list;
	}

	@Override
	public PSystemConfiguration findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PSystemConfiguration.class);
		crit.add(Restrictions.eq("id", id));
		PSystemConfiguration systemConf = (PSystemConfiguration) crit.uniqueResult();
		return systemConf;
	}

	@Override
	public PSystemConfiguration update(PSystemConfiguration systemConfig) {
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
	public void save(PSystemConfiguration systemConfig) {
		sessionFactory.getCurrentSession().save(systemConfig);
	}
}

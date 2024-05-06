package com.soin.sgrm.dao.pos;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Ambient;
import com.soin.sgrm.model.pos.PAmbient;

@Repository
public class PAmbientDaoImpl implements PAmbientDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PAmbient> list(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PAmbient.class);
		crit.createCriteria("system").add(Restrictions.eq("id", id));
		List<PAmbient> list = crit.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PAmbient> list(String search, String system) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PAmbient.class);
		crit.createAlias("system", "system");
		crit.add(Restrictions.eq("system.code", system));
		crit.setMaxResults(100);

		List<PAmbient> environmentList = crit.list();
		return environmentList;
	}

	@Override
	public void addReleaseAmbient(Integer release_id, Integer ambient_id) throws SQLException {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sql = String.format(
					" INSERT INTO \"RELEASES_RELEASE_AMBIENTES\" ( \"ID\", \"RELEASE_ID\", \"AMBIENTE_ID\" ) VALUES (null, %s , %s )",
					release_id, ambient_id);
			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();

			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "ambient");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}

	}

	@Override
	public void deleteReleaseAmbient(Integer release_id, Integer ambient_id) throws SQLException {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sql = String.format("  DELETE FROM \"RELEASES_RELEASE_AMBIENTES\" WHERE \"RELEASE_ID\" = %s AND \"AMBIENTE_ID\" = %s ",
					release_id, ambient_id);
			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();

			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "ambient");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}

	}

	@Override
	public PAmbient findById(Integer id, String system) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PAmbient.class);
		crit.add(Restrictions.eq("id", id));
		crit.createAlias("system", "system");
		crit.add(Restrictions.eq("system.code", system));
		PAmbient ambient = (PAmbient) crit.uniqueResult();
		return ambient;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PAmbient> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PAmbient.class);
		List<PAmbient> list = crit.list();
		return list;
	}

	@Override
	public PAmbient findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PAmbient.class);
		crit.add(Restrictions.eq("id", id));
		return (PAmbient) crit.uniqueResult();
	}

	@Override
	public void save(PAmbient ambient) {
		sessionFactory.getCurrentSession().save(ambient);
	}

	@Override
	public void update(PAmbient ambient) {
		try {
			sessionFactory.getCurrentSession().update(ambient);
		} catch (Exception e) {
			Sentry.capture(e, "ambient");
		}
		
	}

	@Override
	public void delete(Integer id) {
		PAmbient ambient = findById(id);
		sessionFactory.getCurrentSession().delete(ambient);
	}

}

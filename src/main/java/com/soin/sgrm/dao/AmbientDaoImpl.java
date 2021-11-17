package com.soin.sgrm.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.DocsTemplate;
import com.soin.sgrm.model.Ambient;
import com.soin.sgrm.model.Request;

@Repository
public class AmbientDaoImpl implements AmbientDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Ambient> list(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Ambient.class);
		crit.createCriteria("system").add(Restrictions.eq("id", id));
		List<Ambient> list = crit.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Ambient> list(String search, String system) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Ambient.class);
		crit.createAlias("system", "system");
		crit.add(Restrictions.eq("system.code", system));
		crit.setMaxResults(100);

		List<Ambient> environmentList = crit.list();
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
					" INSERT INTO releases_release_ambientes ( id, release_id, ambiente_id ) VALUES (null, %s , %s )",
					release_id, ambient_id);
			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();

			transObj.commit();
		} catch (Exception e) {
			e.printStackTrace();
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
			sql = String.format("  DELETE FROM releases_release_ambientes WHERE release_id = %s AND ambiente_id = %s ",
					release_id, ambient_id);
			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();

			transObj.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}

	}

	@Override
	public Ambient findById(Integer id, String system) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Ambient.class);
		crit.add(Restrictions.eq("id", id));
		crit.createAlias("system", "system");
		crit.add(Restrictions.eq("system.code", system));
		Ambient ambient = (Ambient) crit.uniqueResult();
		return ambient;
	}

}

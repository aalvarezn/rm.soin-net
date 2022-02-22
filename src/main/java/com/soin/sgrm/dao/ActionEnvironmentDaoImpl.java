package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.ActionEnvironment;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseActionEdit;

@Repository
public class ActionEnvironmentDaoImpl implements ActionEnvironmentDao {

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<ActionEnvironment> listBySystem(Integer id) {
		List<ActionEnvironment> list = sessionFactory.getCurrentSession().createCriteria(ActionEnvironment.class)
				.add(Restrictions.eq("system.id", id)).list();
		return list;
	}

	@Override
	public ReleaseActionEdit findById(Integer id) {
		ReleaseActionEdit action = (ReleaseActionEdit) sessionFactory.getCurrentSession().get(ReleaseActionEdit.class,
				id);
		return action;
	}

	@Override
	public ReleaseActionEdit addReleaseAction(ReleaseActionEdit action, Integer release_id) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.saveOrUpdate(action);
			sql = String.format(
					" INSERT INTO releases_release_accion ( id, accion_id, release_id ) VALUES (null, %s , %s )",
					action.getId(), release_id);
			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();
			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "action");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
		return action;

	}

	@Override
	public void deleteReleaseDependency(Integer action_id, Integer release_id) {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sql = String.format("  DELETE FROM releases_release_accion WHERE accion_id = %s AND release_id = %s ",
					action_id, release_id);
			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();
			
			ReleaseActionEdit action =  findById(action_id);
			sessionObj.delete(action);
			
			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "action");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ActionEnvironment> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ActionEnvironment.class);
		return crit.list();
	}

	@Override
	public ActionEnvironment findActionById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ActionEnvironment.class);
		crit.add(Restrictions.eq("id", id));
		return (ActionEnvironment) crit.uniqueResult();
	}

	@Override
	public void save(ActionEnvironment action) {
		sessionFactory.getCurrentSession().save(action);
	}

	@Override
	public void update(ActionEnvironment action) {
		sessionFactory.getCurrentSession().update(action);
	}

	@Override
	public void delete(Integer id) {
		ActionEnvironment action = findActionById(id);
		sessionFactory.getCurrentSession().delete(action);
	}

}

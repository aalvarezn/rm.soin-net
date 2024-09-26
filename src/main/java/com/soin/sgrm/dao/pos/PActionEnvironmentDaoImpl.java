package com.soin.sgrm.dao.pos;

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
import com.soin.sgrm.model.ActionEnvironment;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseActionEdit;
import com.soin.sgrm.model.pos.PActionEnvironment;
import com.soin.sgrm.model.pos.PReleaseActionEdit;

@Repository
public class PActionEnvironmentDaoImpl implements PActionEnvironmentDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PActionEnvironment> listBySystem(Integer id) {
		List<PActionEnvironment> list = sessionFactory.getCurrentSession().createCriteria(PActionEnvironment.class)
				.add(Restrictions.eq("system.id", id)).list();
		return list;
	}

	@Override
	public PReleaseActionEdit findById(Integer id) {
		PReleaseActionEdit action = (PReleaseActionEdit) sessionFactory.getCurrentSession().get(PReleaseActionEdit.class,
				id);
		return action;
	}

	@Override
	public PReleaseActionEdit addReleaseAction(PReleaseActionEdit action, Integer release_id) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.saveOrUpdate(action);
			sessionObj.flush();
			sql = String.format(
					" INSERT INTO \"RELEASES_RELEASE_ACCION\" (  \"ACCION_ID\", \"RELEASE_ID\" ) VALUES ( %s , %s )",
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
		String sql = "RELEASE_ID";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sql = String.format("  DELETE FROM \"RELEASES_RELEASE_ACCION\" WHERE \"ACCION_ID\" = %s AND \"RELEASE_ID\" = %s ",
					action_id, release_id);
			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();
			
			PReleaseActionEdit action =  findById(action_id);
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
	public List<PActionEnvironment> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PActionEnvironment.class);
		return crit.list();
	}

	@Override
	public PActionEnvironment findActionById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PActionEnvironment.class);
		crit.add(Restrictions.eq("id", id));
		return (PActionEnvironment) crit.uniqueResult();
	}

	@Override
	public void save(PActionEnvironment action) {
		sessionFactory.getCurrentSession().save(action);
	}

	@Override
	public void update(PActionEnvironment action) {
		sessionFactory.getCurrentSession().update(action);
	}

	@Override
	public void delete(Integer id) {
		PActionEnvironment action = findActionById(id);
		sessionFactory.getCurrentSession().delete(action);
	}

}

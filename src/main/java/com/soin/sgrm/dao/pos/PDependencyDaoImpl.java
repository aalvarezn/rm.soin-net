package com.soin.sgrm.dao.pos;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;

import com.soin.sgrm.model.pos.PDependency;
import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.model.pos.PReleaseEdit;

@Repository
public class PDependencyDaoImpl implements PDependencyDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@Override
	public PDependency findDependencyById(Integer from_id, Integer to_id, Boolean isFunctional) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PDependency.class);
		crit.add(Restrictions.eq("release.id", from_id)).add(Restrictions.eq("to_release.id", to_id))
				.add(Restrictions.eq("isFunctional", isFunctional));
		return (PDependency) crit.uniqueResult();
	}

	@Override
	public PDependency save(PRelease release, PDependency dependency) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			for (PDependency item : release.getDependencies()) {
				if (dependency.equals(item)) {
					dependency.setId(item.getId());
				}
			}
			sessionObj.saveOrUpdate(dependency);
			transObj.commit();
			return dependency;
		} catch (Exception e) {
			Sentry.capture(e, "dependency");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public void delete(PDependency dependency) {
		sessionFactory.getCurrentSession().delete(dependency);
	}

	@Override
	public ArrayList<PDependency> save(PReleaseEdit release, ArrayList<PDependency> dependencies) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			for (PDependency dependency : dependencies) {
				for (PDependency item : release.getDependencies()) {
					if (dependency.equals(item)) {
						dependency.setId(item.getId());
					}
				}
				sessionObj.saveOrUpdate(dependency);
			}
			transObj.commit();
			return dependencies;
		} catch (Exception e) {
			Sentry.capture(e, "dependency");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

}

package com.soin.sgrm.dao;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Dependency;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseObject;

@Repository
public class DependencyDaoImpl implements DependencyDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Dependency findDependencyById(Integer from_id, Integer to_id, Boolean isFunctional) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Dependency.class);
		crit.add(Restrictions.eq("release.id", from_id)).add(Restrictions.eq("to_release.id", to_id))
				.add(Restrictions.eq("isFunctional", isFunctional));
		return (Dependency) crit.uniqueResult();
	}

	@Override
	public Dependency save(Release release, Dependency dependency) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			for (Dependency item : release.getDependencies()) {
				if (dependency.equals(item)) {
					dependency.setId(item.getId());
				}
			}
			sessionObj.saveOrUpdate(dependency);
			transObj.commit();
			return dependency;
		} catch (Exception e) {
			e.printStackTrace();
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public void delete(Dependency dependency) {
		sessionFactory.getCurrentSession().delete(dependency);
	}

	@Override
	public ArrayList<Dependency> save(Release release, ArrayList<Dependency> dependencies) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			for (Dependency dependency : dependencies) {
				for (Dependency item : release.getDependencies()) {
					if (dependency.equals(item)) {
						dependency.setId(item.getId());
					}
				}
				System.out.println("VERIFICAR: " + dependency.toString());
				sessionObj.saveOrUpdate(dependency);
			}
			transObj.commit();
			return dependencies;
		} catch (Exception e) {
			e.printStackTrace();
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

}

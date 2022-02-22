package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.ConfigurationItem;
import com.soin.sgrm.model.TypeObject;

@Repository
public class TypeObjectDaoImpl implements TypeObjectDao {

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<TypeObject> listBySystem(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(TypeObject.class);
		crit.add(Restrictions.eq("system.id", id));
		List<TypeObject> list = crit.list();
		return list;
	}

	@Override
	public boolean existTypeObject(String name, Integer system_id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(TypeObject.class);
		crit.add(Restrictions.eq("name", name));
		crit.add(Restrictions.eq("system.id", system_id));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return (count > 0);
	}

	@Override
	public TypeObject findByName(String name, Integer system_id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(TypeObject.class);
		crit.add(Restrictions.eq("name", name));
		crit.add(Restrictions.eq("system.id", system_id));
		crit.setMaxResults(1);
		TypeObject object = (TypeObject) crit.uniqueResult();
		return object;
	}

	@Override
	public TypeObject findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(TypeObject.class);
		crit.add(Restrictions.eq("id", id));
		TypeObject object = (TypeObject) crit.uniqueResult();
		return object;
	}

	@Override
	public TypeObject findByName(String name, String extension, Integer system_id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(TypeObject.class);

		Criterion nameRest = Restrictions.eq("name", name).ignoreCase();
		Criterion extensionRest = Restrictions.like("extension", extension, MatchMode.END).ignoreCase();

		if (!extension.equals("")) {
			crit.add(Restrictions.or(nameRest, extensionRest));
		} else {
			crit.add(nameRest);
		}
		crit.add(Restrictions.eq("system.id", system_id));
		crit.setMaxResults(1);
		TypeObject object = (TypeObject) crit.uniqueResult();
		return object;
	}

	@Override
	public TypeObject save(TypeObject type) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.saveOrUpdate(type);
			transObj.commit();
			return type;
		} catch (Exception e) {
			Sentry.capture(e, "typeObject");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TypeObject> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(TypeObject.class);
		return crit.list();
	}

	@Override
	public void update(TypeObject typeObject) {
		sessionFactory.getCurrentSession().update(typeObject);
	}

	@Override
	public void delete(Integer id) {
		TypeObject typeObject = findById(id);
		sessionFactory.getCurrentSession().delete(typeObject);
	}

}

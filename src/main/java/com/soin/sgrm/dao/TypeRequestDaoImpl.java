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
import com.soin.sgrm.model.TypeRequest;

@Repository
public class TypeRequestDaoImpl implements TypeRequestDao {

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<TypeRequest> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(TypeRequest.class);
		return crit.list();
	}

	@Override
	public TypeRequest findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(TypeRequest.class);
		crit.add(Restrictions.eq("id", id));
		return (TypeRequest) crit.uniqueResult();
	}

	@Override
	public TypeRequest save(TypeRequest typeRequest) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			sessionObj.saveOrUpdate(typeRequest);
			transObj.commit();
			return typeRequest;
		} catch (Exception e) {
			Sentry.capture(e, "typeRequest");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public void update(TypeRequest typeRequest) {
		sessionFactory.getCurrentSession().update(typeRequest);
	}

	@Override
	public void delete(Integer id) {
		TypeRequest typeRequest = findById(id);
		sessionFactory.getCurrentSession().delete(typeRequest);
	}

}

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
import com.soin.sgrm.model.TypeRequest;
import com.soin.sgrm.model.pos.PTypeRequest;

@Repository
public class PTypeRequestDaoImpl implements PTypeRequestDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PTypeRequest> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PTypeRequest.class);
		return crit.list();
	}

	@Override
	public PTypeRequest findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PTypeRequest.class);
		crit.add(Restrictions.eq("id", id));
		return (PTypeRequest) crit.uniqueResult();
	}

	@Override
	public PTypeRequest save(PTypeRequest typeRequest) {
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
	public void update(PTypeRequest typeRequest) {
		sessionFactory.getCurrentSession().update(typeRequest);
	}

	@Override
	public void delete(Integer id) {
		PTypeRequest typeRequest = findById(id);
		sessionFactory.getCurrentSession().delete(typeRequest);
	}

}

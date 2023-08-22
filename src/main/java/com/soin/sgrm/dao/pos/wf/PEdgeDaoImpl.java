package com.soin.sgrm.dao.pos.wf;

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
import com.soin.sgrm.model.pos.wf.PEdge;
import com.soin.sgrm.model.pos.wf.PEdgeIncidence;
import com.soin.sgrm.model.pos.wf.PEdgeRFC;

@Repository
public class PEdgeDaoImpl implements PEdgeDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;
	@SuppressWarnings("unchecked")
	@Override
	public List<PEdge> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PEdge.class);
		return crit.list();
	}

	@Override
	public PEdge findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PEdge.class);
		crit.add(Restrictions.eq("id", id));
		return (PEdge) crit.uniqueResult();
	}

	@Override
	public PEdge save(PEdge edge) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.save(edge);
			transObj.commit();
			return edge;
		} catch (Exception e) {
			Sentry.capture(e, "edges");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public PEdge update(PEdge edge) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.update(edge);
			transObj.commit();
			return edge;
		} catch (Exception e) {
			Sentry.capture(e, "edges");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public void delete(Integer id) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			PEdge edge = findById(id);
			sessionObj.delete(edge);

			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "edges");
			transObj.rollback();
			throw new Exception("Error al procesar la solucitud de eliminar.", e);
		} finally {
			sessionObj.close();
		}

	}
	@SuppressWarnings("unchecked")
	@Override
	public List<PEdgeRFC> listEdgeRFC() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PEdgeRFC.class);
		return crit.list();
	}

	@Override
	public PEdgeRFC findByIdEdgeRFC(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PEdgeRFC.class);
		crit.add(Restrictions.eq("id", id));
		return (PEdgeRFC) crit.uniqueResult();
	}

	@Override
	public PEdgeRFC saveEdgeRFC(PEdgeRFC edge) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.save(edge);
			transObj.commit();
			return edge;
		} catch (Exception e) {
			Sentry.capture(e, "PEdgeRFC");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public PEdgeRFC updateEdgeRFC(PEdgeRFC edge) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.update(edge);
			transObj.commit();
			return edge;
		} catch (Exception e) {
			Sentry.capture(e, "PEdgeRFC");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public void deleteEdgeRFC(Integer id) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			PEdgeRFC edge = findByIdEdgeRFC(id);
			sessionObj.delete(edge);

			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "edges");
			transObj.rollback();
			throw new Exception("Error al procesar la solicitud de eliminar.", e);
		} finally {
			sessionObj.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PEdgeIncidence> listEdgeIncidence() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PEdgeIncidence.class);
		return crit.list();
	}

	@Override
	public PEdgeIncidence findByIdEdgeIncidence(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PEdgeIncidence.class);
		crit.add(Restrictions.eq("id", id));
		return (PEdgeIncidence) crit.uniqueResult();
	}

	@Override
	public PEdgeIncidence saveEdgeIncidence(PEdgeIncidence edge) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.save(edge);
			transObj.commit();
			return edge;
		} catch (Exception e) {
			Sentry.capture(e, "edgeIncidence");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public PEdgeIncidence updateEdgeIncidence(PEdgeIncidence edge) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.update(edge);
			transObj.commit();
			return edge;
		} catch (Exception e) {
			Sentry.capture(e, "edgeIncidence");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public void deleteEdgeIncidence(Integer id) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			PEdgeIncidence edge = findByIdEdgeIncidence(id);
			sessionObj.delete(edge);

			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "edges");
			transObj.rollback();
			throw new Exception("Error al procesar la solucitud de eliminar.", e);
		} finally {
			sessionObj.close();
		}
	}
}
package com.soin.sgrm.dao.wf;

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
import com.soin.sgrm.model.wf.Edge;
import com.soin.sgrm.model.wf.EdgeRFC;

@Repository
public class EdgeDaoImpl implements EdgeDao {

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Edge> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Edge.class);
		return crit.list();
	}

	@Override
	public Edge findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Edge.class);
		crit.add(Restrictions.eq("id", id));
		return (Edge) crit.uniqueResult();
	}

	@Override
	public Edge save(Edge edge) {
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
	public Edge update(Edge edge) {
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
			Edge edge = findById(id);
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
	public List<EdgeRFC> listEdgeRFC() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(EdgeRFC.class);
		return crit.list();
	}

	@Override
	public EdgeRFC findByIdEdgeRFC(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(EdgeRFC.class);
		crit.add(Restrictions.eq("id", id));
		return (EdgeRFC) crit.uniqueResult();
	}

	@Override
	public EdgeRFC saveEdgeRFC(EdgeRFC edge) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.save(edge);
			transObj.commit();
			return edge;
		} catch (Exception e) {
			Sentry.capture(e, "EdgeRFC");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public EdgeRFC updateEdgeRFC(EdgeRFC edge) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.update(edge);
			transObj.commit();
			return edge;
		} catch (Exception e) {
			Sentry.capture(e, "EdgeRFC");
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
			EdgeRFC edge = findByIdEdgeRFC(id);
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

}

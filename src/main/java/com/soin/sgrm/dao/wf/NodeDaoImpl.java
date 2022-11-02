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
import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.wf.Node;
import com.soin.sgrm.model.wf.NodeIncidence;
import com.soin.sgrm.model.wf.WorkFlow;

@Repository
public class NodeDaoImpl implements NodeDao {

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Node> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Node.class);
		return crit.list();
	}

	@Override
	public Node findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Node.class);
		crit.add(Restrictions.eq("id", id));
		return (Node) crit.uniqueResult();
	}

	@Override
	public Node save(Node node) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.save(node);
			transObj.commit();
			return node;
		} catch (Exception e) {
			Sentry.capture(e, "nodes");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public Node update(Node node) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.update(node);
			transObj.commit();
			return node;
		} catch (Exception e) {
			Sentry.capture(e, "nodes");
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
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sql = String.format("DELETE FROM TRAMITES_NODO WHERE ID = %s ", id);
			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();
			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "nodes");
			transObj.rollback();
			throw new Exception("Error al procesar la solucitud de eliminar.", e);
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public Node existWorkFlow(Release release) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Node.class);
		crit.createAlias("workFlow", "workFlow");
		crit.createAlias("workFlow.system", "system");
		crit.createAlias("workFlow.type", "type");
		// Que sea de tipo inicio
		crit.add(Restrictions.eq("group", "start"));
		crit.add(Restrictions.eq("type.id", 1));
		// Que sea del mismo sistema
		crit.add(Restrictions.eq("system.id", release.getSystem().getId()));
		return (Node) crit.uniqueResult();
	}

	@Override
	public NodeIncidence saveNodeIncidence(NodeIncidence node) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.save(node);
			transObj.commit();
			return node;
		} catch (Exception e) {
			Sentry.capture(e, "nodeIncidence");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public NodeIncidence existWorkFlowNodeIn(Incidence incidence) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(NodeIncidence.class);
		crit.createAlias("workFlow", "workFlow");
		crit.createAlias("workFlow.system", "system");
		crit.createAlias("workFlow.type", "type");

		// Que sea de tipo inicio
		crit.add(Restrictions.eq("group", "start"));
		crit.add(Restrictions.eq("type.id", 2));
		// Que sea del mismo sistema
		crit.add(Restrictions.eq("system.id", incidence.getSystem().getId()));
		return (NodeIncidence) crit.uniqueResult();
	}

	@Override
	public void deleteNodeIncidence(Integer id) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sql = String.format("DELETE FROM TRAMITES_NODO_INC WHERE ID = %s ", id);
			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();
			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "nodeIncidence");
			transObj.rollback();
			throw new Exception("Error al procesar la solucitud de eliminar.", e);
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public NodeIncidence updateNodeIncidence(NodeIncidence node) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.update(node);
			transObj.commit();
			return node;
		} catch (Exception e) {
			Sentry.capture(e, "nodeIncidence");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public NodeIncidence findByIdNoInci(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(NodeIncidence.class);
		crit.add(Restrictions.eq("id", id));
		return (NodeIncidence) crit.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NodeIncidence> listNodeIncidence() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Node.class);
		return crit.list();
	}
}

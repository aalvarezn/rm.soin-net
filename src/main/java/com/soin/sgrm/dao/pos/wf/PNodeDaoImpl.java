package com.soin.sgrm.dao.pos.wf;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.pos.PIncidence;
import com.soin.sgrm.model.pos.PRFC;
import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.model.pos.wf.PNode;
import com.soin.sgrm.model.pos.wf.PNodeIncidence;
import com.soin.sgrm.model.pos.wf.PNodeRFC;
import com.soin.sgrm.model.wf.Node;
import com.soin.sgrm.model.wf.NodeName;

@Repository
public class PNodeDaoImpl implements PNodeDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PNode> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PNode.class);
		return crit.list();
	}

	@Override
	public PNode findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PNode.class);
		crit.add(Restrictions.eq("id", id));
		return (PNode) crit.uniqueResult();
	}

	@Override
	public PNode save(PNode node) {
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
	public PNode update(PNode node) {
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
			sql = String.format("DELETE FROM \"TRAMITES_NODO\" WHERE \"ID\" = %s ", id);
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
	public PNode existWorkFlow(PRelease release) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PNode.class);
		crit.createAlias("workFlow", "workFlow");
		crit.createAlias("workFlow.system", "system");
		crit.createAlias("workFlow.type", "type");
		// Que sea de tipo inicio
		crit.add(Restrictions.eq("group", "start"));
		crit.add(Restrictions.eq("type.id", 1));
		// Que sea del mismo sistema
		crit.add(Restrictions.eq("system.id", release.getSystem().getId()));
		return (PNode) crit.uniqueResult();
	}
	@Override
	public PNodeRFC saveNodeRFC(PNodeRFC node) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.save(node);
			transObj.commit();
			return node;
		} catch (Exception e) {
			Sentry.capture(e, "PNodeRFC");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public PNodeRFC existWorkFlowNodeRFC(PRFC rfc) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PNodeRFC.class);
		crit.createAlias("workFlow", "workFlow");
		crit.createAlias("workFlow.system", "system");
		crit.createAlias("workFlow.type", "type");

		// Que sea de tipo inicio
		crit.add(Restrictions.eq("group", "start"));
		crit.add(Restrictions.eq("type.id", 3));
		// Que sea del mismo sistema
		crit.add(Restrictions.eq("system.id", rfc.getSystemInfo().getId()));
		return (PNodeRFC) crit.uniqueResult();
	}

	@Override
	public void deleteNodeRFC(Integer id) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sql = String.format("DELETE FROM \"TRAMITES_NODO_RFC\" WHERE \"ID\" = %s ", id);
			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();
			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "PNodeRFC");
			transObj.rollback();
			throw new Exception("Error al procesar la solicitud de eliminar.", e);
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public PNodeRFC updateNodeRFC(PNodeRFC node) {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.update(node);
			transObj.commit();
			return node;
		} catch (Exception e) {
			Sentry.capture(e, "PNodeRFC");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public PNodeRFC findByIdNoRFC(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PNodeRFC.class);
		crit.add(Restrictions.eq("id", id));
		return (PNodeRFC) crit.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PNodeRFC> listNodeRFC() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PNode.class);
		return crit.list();
	}
	@Override
	public boolean verifyStartNodeRFC(PNodeRFC node) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PNodeRFC.class);
		crit.createAlias("workFlow", "workFlow");
		crit.add(Restrictions.eq("workFlow.id", node.getWorkFlowId()));
		crit.add(Restrictions.eq("group","start"));
		crit.add(Restrictions.not(Restrictions.eq("id",node.getId())));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		if(count==0) {
			return false;
			
		}else {
			return true;
		}
	}

		@Override
	public PNodeIncidence saveNodeIncidence(PNodeIncidence node) {
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
	public PNodeIncidence existWorkFlowNodeIn(PIncidence incidence) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PNodeIncidence.class);
		crit.createAlias("workFlow", "workFlow");
		crit.createAlias("workFlow.system", "system");
		crit.createAlias("workFlow.type", "type");

		// Que sea de tipo inicio
		crit.add(Restrictions.eq("group", "start"));
		crit.add(Restrictions.eq("type.id", 2));
		// Que sea del mismo sistema
		crit.add(Restrictions.eq("system.id", incidence.getSystem().getId()));
		return (PNodeIncidence) crit.uniqueResult();
	}

	@Override
	public void deleteNodeIncidence(Integer id) {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sql = String.format("DELETE FROM \"TRAMITES_NODO_INC\" WHERE \"ID\" = %s ", id);
			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();
			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "nodeIncidence");
			transObj.rollback();
		} finally {
			sessionObj.close();
		}
	}

	@Override
	public PNodeIncidence updateNodeIncidence(PNodeIncidence node) {
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
	public PNodeIncidence findByIdNoInci(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PNodeIncidence.class);
		crit.add(Restrictions.eq("id", id));
		return (PNodeIncidence) crit.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PNodeIncidence> listNodeIncidence() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PNode.class);
		return crit.list();
	}
	@Override
	public boolean verifyStartNodeIncidence(PNodeIncidence node) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PNodeIncidence.class);
		crit.createAlias("workFlow", "workFlow");
		crit.add(Restrictions.eq("workFlow.id", node.getWorkFlowId()));
		crit.add(Restrictions.eq("group","start"));
		crit.add(Restrictions.not(Restrictions.eq("id",node.getId())));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		if(count==0) {
			return false;
			
		}else {
			return true;
		}
	}
	@Override
	public boolean verifyStartNode(PNode node) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PNode.class);
		crit.createAlias("workFlow", "workFlow");
		crit.add(Restrictions.eq("workFlow.id", node.getWorkFlowId()));
		crit.add(Restrictions.eq("group","start"));
		crit.add(Restrictions.not(Restrictions.eq("id",node.getId())));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		if(count==0) {
			return false;
			
		}else {
			return true;
		}
	}

	@Override
	public PNode findByIdAndWorkFlow(String nodeName, Integer workFlowId) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Node.class);
		crit.createAlias("workFlow", "workFlow");
		crit.add(Restrictions.eq("workFlow.id",workFlowId));
		crit.add(Restrictions.eq("label",nodeName));
		return (PNode) crit.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NodeName> listNodeNames(Integer idProyecto, Integer userLogin) {
		List<NodeName>listNode=new ArrayList<NodeName>();
		String sql = "ID";
		Query query = null;
		sql = String.format(
				"SELECT DISTINCT tn.\"NOMBRE\"\r\n" + 
				"FROM \"TRAMITES_NODO\" tn\r\n" + 
				"INNER JOIN \"TRAMITES_NODO\" tt ON tn.\"TRAMITE_ID\" = tt.\"ID\"\r\n" + 
				"INNER JOIN \"SISTEMAS_SISTEMA\" ss ON tt.\"SISTEMA_ID\" = ss.\"ID\"\r\n" +
				"INNER JOIN  \"TRAMITES_NODO_ACTOR\" tna ON tna.\"NODO_ID\" =tn.\"ID\"\r\n"+
				"WHERE tn.\"GRUPO\" = 'action' AND ss.\"PROYECTO_ID\" =%s",
				idProyecto);
		query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		listNode=query.list();
		return listNode;
	}


}
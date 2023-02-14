package com.soin.sgrm.dao.wf;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.wf.WorkFlow;
import com.soin.sgrm.model.wf.WorkFlowIncidence;
//import com.soin.sgrm.model.wf.WorkFlowIncidence;
import com.soin.sgrm.model.wf.WorkFlowRFC;

@Repository
public class WorkFlowDaoImlp implements WorkFlowDao {

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkFlow> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(WorkFlow.class);
		return crit.list();
	}

	@Override
	public WorkFlow findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(WorkFlow.class);
		crit.add(Restrictions.eq("id", id));
		return (WorkFlow) crit.uniqueResult();
	}

	@Override
	public void save(WorkFlow workFlow) {
		sessionFactory.getCurrentSession().save(workFlow);
	}

	@Override
	public void update(WorkFlow workFlow) {
		sessionFactory.getCurrentSession().update(workFlow);
	}

	@Override
	public void delete(Integer id) {
		WorkFlow workFlow = findById(id);
		sessionFactory.getCurrentSession().delete(workFlow);
	}
	@Override
	public WorkFlowIncidence findByIdIncidence(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(WorkFlowIncidence.class);
		crit.add(Restrictions.eq("id", id));
		return (WorkFlowIncidence) crit.uniqueResult();
	}

	@Override
	public boolean verifyCreation(Integer systemId, Integer typeId) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(WorkFlow.class);
		crit.createAlias("system", "system");
		crit.createAlias("type", "type");
		crit.add(Restrictions.eq("system.id", systemId));
		crit.add(Restrictions.eq("type.id", typeId));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		if(count==0) {
			return false;
			
		}else {
			return true;
		}
	}

	@Override
	public WorkFlowRFC findByIdRFC(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(WorkFlowRFC.class);
		crit.add(Restrictions.eq("id", id));
		return (WorkFlowRFC) crit.uniqueResult();
	}

}
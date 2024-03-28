package com.soin.sgrm.dao.pos.wf;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.wf.PWorkFlow;
import com.soin.sgrm.model.pos.wf.PWorkFlowIncidence;
import com.soin.sgrm.model.pos.wf.PWorkFlowRFC;


@Repository
public class PWorkFlowDaoImlp implements PWorkFlowDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PWorkFlow> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PWorkFlow.class);
		return crit.list();
	}

	@Override
	public PWorkFlow findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PWorkFlow.class);
		crit.add(Restrictions.eq("id", id));
		return (PWorkFlow) crit.uniqueResult();
	}

	@Override
	public void save(PWorkFlow workFlow) {
		sessionFactory.getCurrentSession().save(workFlow);
	}

	@Override
	public void update(PWorkFlow workFlow) {
		sessionFactory.getCurrentSession().update(workFlow);
	}

	@Override
	public void delete(Integer id) {
		PWorkFlow workFlow = findById(id);
		sessionFactory.getCurrentSession().delete(workFlow);
	}
	@Override
	public PWorkFlowIncidence findByIdIncidence(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PWorkFlowIncidence.class);
		crit.add(Restrictions.eq("id", id));
		return (PWorkFlowIncidence) crit.uniqueResult();
	}

	@Override
	public boolean verifyCreation(Integer systemId, Integer typeId) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PWorkFlow.class);
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
	public PWorkFlowRFC findByIdRFC(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PWorkFlowRFC.class);
		crit.add(Restrictions.eq("id", id));
		return (PWorkFlowRFC) crit.uniqueResult();
	}

}
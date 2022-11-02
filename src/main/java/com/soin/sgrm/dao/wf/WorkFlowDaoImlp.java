package com.soin.sgrm.dao.wf;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.wf.WorkFlow;
import com.soin.sgrm.model.wf.WorkFlowIncidence;

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

}

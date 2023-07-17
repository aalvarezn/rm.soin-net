package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.pos.PPriority;

@Repository
public class PPriorityDaoImpl implements PPriorityDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PPriority> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PPriority.class);
		return crit.list();
	}

	@Override
	public PPriority findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PPriority.class);
		crit.add(Restrictions.eq("id", id));
		return (PPriority) crit.uniqueResult();
	}

	@Override
	public void save(PPriority priority) {
		sessionFactory.getCurrentSession().save(priority);
	}

	@Override
	public void update(PPriority priority) {
		sessionFactory.getCurrentSession().update(priority);
	}

	@Override
	public void delete(Integer id) {
		PPriority priority = findById(id);
		sessionFactory.getCurrentSession().delete(priority);
	}

}

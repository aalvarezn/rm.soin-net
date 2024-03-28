package com.soin.sgrm.dao.pos.wf;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.wf.PType;

@Repository
public class PTypeDaoImlp implements PTypeDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PType> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PType.class);
		return crit.list();
	}

	@Override
	public PType findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PType.class);
		crit.add(Restrictions.eq("id", id));
		return (PType) crit.uniqueResult();
	}

	@Override
	public void save(PType type) {
		sessionFactory.getCurrentSession().save(type);
	}

	@Override
	public void update(PType type) {
		sessionFactory.getCurrentSession().update(type);
	}

	@Override
	public void delete(Integer id) {
		PType type = findById(id);
		sessionFactory.getCurrentSession().delete(type);
	}

}

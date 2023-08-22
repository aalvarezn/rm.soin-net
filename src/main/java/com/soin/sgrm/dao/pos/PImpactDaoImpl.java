package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.pos.PImpact;

@Repository
public class PImpactDaoImpl implements PImpactDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PImpact> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PImpact.class);
		List<PImpact> list = crit.list();
		return list;
	}

	@Override
	public PImpact findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PImpact.class);
		crit.add(Restrictions.eq("id", id));
		return (PImpact) crit.uniqueResult();
	}

	@Override
	public void save(PImpact impact) {
		sessionFactory.getCurrentSession().save(impact);
	}

	@Override
	public void update(PImpact impact) {
		sessionFactory.getCurrentSession().update(impact);
	}

	@Override
	public void delete(Integer id) {
		PImpact impact = findById(id);
		sessionFactory.getCurrentSession().delete(impact);
	}

}

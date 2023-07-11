package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Environment;
import com.soin.sgrm.model.pos.PEnvironment;

@Repository
public class PEnvironmentDaoImpl implements PEnvironmentDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PEnvironment> listBySystem(Integer id) {
		List<PEnvironment> list = sessionFactory.getCurrentSession().createCriteria(PEnvironment.class)
				.add(Restrictions.eq("system.id", id)).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PEnvironment> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PEnvironment.class);
		return crit.list();
	}

	@Override
	public PEnvironment findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PEnvironment.class);
		crit.add(Restrictions.eq("id", id));
		return (PEnvironment) crit.uniqueResult();
	}

	@Override
	public void save(PEnvironment environment) {
		sessionFactory.getCurrentSession().save(environment);
	}

	@Override
	public void update(PEnvironment environment) {
		sessionFactory.getCurrentSession().update(environment);
	}

	@Override
	public void delete(Integer id) {
		PEnvironment environment = findById(id);
		sessionFactory.getCurrentSession().delete(environment);
	}

}

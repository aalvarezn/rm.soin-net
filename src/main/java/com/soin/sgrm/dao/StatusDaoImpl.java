package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.Status;

@Repository
public class StatusDaoImpl implements StatusDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Status> list() {
		List<Status> releaseStatusList = sessionFactory.getCurrentSession().createQuery("from ReleaseStatus").list();
		return releaseStatusList;
	}

	@Override
	public Status findByName(String name) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Status.class);
		crit.add(Restrictions.eq("name", name));
		Status status = (Status) crit.uniqueResult();
		return status;
	}

	@Override
	public Status findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Status.class);
		crit.add(Restrictions.eq("id", id));
		return (Status) crit.uniqueResult();
	}

	@Override
	public void save(Status status) {
		sessionFactory.getCurrentSession().save(status);
	}

	@Override
	public void update(Status status) {
		sessionFactory.getCurrentSession().update(status);
	}

	@Override
	public void delete(Integer id) {
		Status status = findById(id);
		sessionFactory.getCurrentSession().delete(status);
	}

}

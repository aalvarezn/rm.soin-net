package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Authority;

@Repository
public class AuthorityDaoImpl implements AuthorityDao {

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Authority> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Authority.class);
		List<Authority> list = crit.list();
		return list;
	}

	@Override
	public Authority findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Authority.class);
		crit.add(Restrictions.eq("id", id));
		return (Authority) crit.uniqueResult();
	}

}

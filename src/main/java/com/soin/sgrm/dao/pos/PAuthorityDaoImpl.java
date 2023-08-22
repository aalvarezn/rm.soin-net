package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PAuthority;



@Repository
public class PAuthorityDaoImpl implements PAuthorityDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PAuthority> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PAuthority.class);
		List<PAuthority> list = crit.list();
		return list;
	}

	@Override
	public PAuthority findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PAuthority.class);
		crit.add(Restrictions.eq("id", id));
		return (PAuthority) crit.uniqueResult();
	}

	@Override
	public void save(PAuthority authority) {
		sessionFactory.getCurrentSession().save(authority);
	}

	@Override
	public void update(PAuthority authority) {
		sessionFactory.getCurrentSession().update(authority);
	}

	@Override
	public void delete(Integer id) {
		PAuthority authority = findById(id);
		sessionFactory.getCurrentSession().delete(authority);
	}

}

package com.soin.sgrm.dao.wf;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.wf.Type;

@Repository
public class TypeDaoImlp implements TypeDao {

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Type> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Type.class);
		return crit.list();
	}

	@Override
	public Type findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Type.class);
		crit.add(Restrictions.eq("id", id));
		return (Type) crit.uniqueResult();
	}

	@Override
	public void save(Type type) {
		sessionFactory.getCurrentSession().save(type);
	}

	@Override
	public void update(Type type) {
		sessionFactory.getCurrentSession().update(type);
	}

	@Override
	public void delete(Integer id) {
		Type type = findById(id);
		sessionFactory.getCurrentSession().delete(type);
	}

}

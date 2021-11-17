package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.TypeDetail;

@Repository
public class TypeDetailDaoImpl implements TypeDetailDao {

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<TypeDetail> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(TypeDetail.class);
		return crit.list();
	}

	@Override
	public TypeDetail findByName(String name) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(TypeDetail.class);
		crit.add(Restrictions.eq("name", name));
		return (TypeDetail) crit.uniqueResult();
	}
}

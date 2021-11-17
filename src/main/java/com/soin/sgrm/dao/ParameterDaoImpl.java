package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Parameter;

@Repository
public class ParameterDaoImpl implements ParameterDao {

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Parameter> listAll() {
		Criteria cri = sessionFactory.getCurrentSession().createCriteria(Parameter.class);
		return cri.list();
	}

	@Override
	public Parameter findByCode(Integer code) {
		Criteria cri = sessionFactory.getCurrentSession().createCriteria(Parameter.class);
		cri.add(Restrictions.eq("code", code));
		return (Parameter) cri.uniqueResult();
	}

	@Override
	public Parameter findById(Integer id) {
		Criteria cri = sessionFactory.getCurrentSession().createCriteria(Parameter.class);
		cri.add(Restrictions.eq("id", id));
		return (Parameter) cri.uniqueResult();
	}

	@Override
	public void updateParameter(Parameter param) {
		sessionFactory.getCurrentSession().update(param);
	}

}

package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PParameter;

@Repository
public class PParameterDaoImpl implements PParameterDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;
	@SuppressWarnings("unchecked")
	@Override
	public List<PParameter> listAll() {
		Criteria cri = sessionFactory.getCurrentSession().createCriteria(PParameter.class);
		return cri.list();
	}

	@Override
	public PParameter findByCode(Integer code) {
		Criteria cri = sessionFactory.getCurrentSession().createCriteria(PParameter.class);
		cri.add(Restrictions.eq("code", code));
		return (PParameter) cri.uniqueResult();
	}

	@Override
	public PParameter findById(Integer id) {
		Criteria cri = sessionFactory.getCurrentSession().createCriteria(PParameter.class);
		cri.add(Restrictions.eq("id", id));
		return (PParameter) cri.uniqueResult();
	}

	@Override
	public void updatePParameter(PParameter param) {
		sessionFactory.getCurrentSession().update(param);
	}

	@Override
	public PParameter getPParameterByCode(Integer code) {
		return  (PParameter) sessionFactory.getCurrentSession().createCriteria(PParameter.class).add(Restrictions.eq("code", code)).uniqueResult();
	}

}

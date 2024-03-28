package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.TypeDetail;
import com.soin.sgrm.model.pos.PTypeDetail;

@Repository
public class PTypeDetailDaoImpl implements PTypeDetailDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PTypeDetail> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(TypeDetail.class);
		return crit.list();
	}

	@Override
	public PTypeDetail findByName(String name) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PTypeDetail.class);
		crit.add(Restrictions.eq("name", name));
		return (PTypeDetail) crit.uniqueResult();
	}
}

package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;


import com.soin.sgrm.model.pos.PStatusRFC;
import com.soin.sgrm.utils.Constant;

@Repository
public class PStatusRFCDaoImpl extends AbstractDao<Long, PStatusRFC> implements PStatusRFCDao{
	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PStatusRFC> findWithFilter() {
		List<PStatusRFC> statuses = sessionFactory.getCurrentSession().createCriteria(PStatusRFC.class)
				.add(Restrictions.not(Restrictions.in("name", Constant.FILTREDRFC))).list();
		return statuses;

	}

}

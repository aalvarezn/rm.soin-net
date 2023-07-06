package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.utils.Constant;

@Repository
public class StatusRFCDaoImpl extends AbstractDao<Long, StatusRFC> implements StatusRFCDao{
	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StatusRFC> findWithFilter() {
		List<StatusRFC> statuses = sessionFactory.getCurrentSession().createCriteria(StatusRFC.class)
				.add(Restrictions.not(Restrictions.in("name", Constant.FILTREDRFC))).list();
		return statuses;

	}

}

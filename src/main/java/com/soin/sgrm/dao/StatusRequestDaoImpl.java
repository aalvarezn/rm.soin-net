package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.StatusRequest;
import com.soin.sgrm.utils.Constant;

@Repository
public class StatusRequestDaoImpl extends AbstractDao<Long, StatusRequest> implements StatusRequestDao {
	@Autowired
	private SessionFactory sessionFactory;
	@SuppressWarnings("unchecked")
	@Override
	public List<StatusRequest> findWithFilter() {
		List<StatusRequest> statuses = sessionFactory.getCurrentSession().createCriteria(StatusRequest.class)
				.add(Restrictions.not(Restrictions.in("name", Constant.FILTREDRFC))).list();
		return statuses;
	}

}

package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.StatusRequest;
import com.soin.sgrm.model.pos.PStatusRequest;
import com.soin.sgrm.utils.Constant;

@Repository
public class PStatusRequestDaoImpl extends AbstractDao<Long, PStatusRequest> implements PStatusRequestDao {
	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;
	@SuppressWarnings("unchecked")
	@Override
	public List<PStatusRequest> findWithFilter() {
		List<PStatusRequest> statuses = sessionFactory.getCurrentSession().createCriteria(PStatusRequest.class)
				.add(Restrictions.not(Restrictions.in("name", Constant.FILTREDRFC))).list();
		return statuses;
	}

}

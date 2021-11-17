package com.soin.sgrm.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.Request;

@Repository
public class RequestDaoImpl implements RequestDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Request> list(String search) throws SQLException {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Request.class);
		crit.setMaxResults(50);

		crit.add(Restrictions.or(Restrictions.like("code_soin", search, MatchMode.ANYWHERE).ignoreCase(),
				Restrictions.like("code_ice", search, MatchMode.ANYWHERE).ignoreCase(),
				Restrictions.like("description", search, MatchMode.ANYWHERE).ignoreCase()));

		List<Request> requestList = crit.list();
		return requestList;
	}

	@Override
	public Request findById(Integer id) {
		return (Request) sessionFactory.getCurrentSession().get(Request.class, id);
	}

	@Override
	public Request findByName(String code_soin, String code_ice) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Request.class);
		crit.add(Restrictions.eqProperty("code_soin", code_soin));
		crit.add(Restrictions.eqProperty("code_ice", code_ice));
		return (Request) crit.uniqueResult();
	}

	@Override
	public Request findByName(String code_soin) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Request.class);
		crit.add(Restrictions.eqProperty("code_soin", code_soin));
		return (Request) crit.uniqueResult();
	}

}

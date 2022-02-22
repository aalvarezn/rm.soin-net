package com.soin.sgrm.dao.wf;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.wf.WFUser;

@Repository
public class WFUserDaoImpl implements WFUserDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<WFUser> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(WFUser.class);
		crit.addOrder(Order.asc("username"));
		List<WFUser> userList = crit.list();
		return userList;
	}

	@Override
	public WFUser findWFUserById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(WFUser.class);
		crit.add(Restrictions.eq("id", id));
		return (WFUser) crit.uniqueResult();
	}
}

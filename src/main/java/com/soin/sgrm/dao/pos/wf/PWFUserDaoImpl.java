package com.soin.sgrm.dao.pos.wf;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.wf.PWFUser;


@Repository
public class PWFUserDaoImpl implements PWFUserDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PWFUser> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PWFUser.class);
		crit.addOrder(Order.asc("username"));
		List<PWFUser> userList = crit.list();
		return userList;
	}

	@Override
	public PWFUser findWFUserById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PWFUser.class);
		crit.add(Restrictions.eq("id", id));
		return (PWFUser) crit.uniqueResult();
	}
}

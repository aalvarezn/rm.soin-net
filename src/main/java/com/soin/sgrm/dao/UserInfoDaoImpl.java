package com.soin.sgrm.dao;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Authority;
import com.soin.sgrm.model.User;
import com.soin.sgrm.model.UserInfo;

@Repository
public class UserInfoDaoImpl implements UserInfoDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<UserInfo> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(UserInfo.class);
		crit.addOrder(Order.asc("username"));
		List<UserInfo> userInfoList = crit.list();
		return userInfoList;
	}

	@Override
	public UserInfo getUserByUsername(String username) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(UserInfo.class);
		crit.add(Restrictions.eq("username", username));
		crit.add(Restrictions.eq("active", true));
		UserInfo user = (UserInfo) crit.uniqueResult();
		return user;
	}

	@Override
	public void changePassword(UserInfo userInfo) throws SQLException {
		sessionFactory.getCurrentSession().update(userInfo);
	}

	@Override
	public UserInfo findUserInfoById(Integer id) {
		UserInfo user = (UserInfo) sessionFactory.getCurrentSession().createCriteria(UserInfo.class)
				.add(Restrictions.eq("id", id)).uniqueResult();
		return user;
	}

	@Override
	public void softDelete(UserInfo userInfo) {
		sessionFactory.getCurrentSession().update(userInfo);
	}

	@Override
	public void updateUserInfo(UserInfo userInfo) {
		sessionFactory.getCurrentSession().update(userInfo);
	}

	@Override
	public boolean uniqueUsername(UserInfo userInfo) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(UserInfo.class);
		crit.add(Restrictions.eq("username", userInfo.getUsername()));
		crit.add(Restrictions.ne("id", userInfo.getId()));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return (count.intValue() == 0) ? true : false;
	}

	@Override
	public UserInfo getUserByEmail(String email) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(UserInfo.class);
		crit.add(Restrictions.eq("emailAddress", email));
		crit.add(Restrictions.eq("active", true));
		UserInfo user = (UserInfo) crit.uniqueResult();
		return user;
	}

	@Override
	public void saveUserInfo(UserInfo userInfo) {
		sessionFactory.getCurrentSession().saveOrUpdate(userInfo);
	}

	@Override
	public void delete(Integer id) {
		UserInfo userInfo = findUserInfoById(id);
		sessionFactory.getCurrentSession().delete(userInfo);
	}

	@Override
	public User findUserById(Integer id) {
		User user = (User) sessionFactory.getCurrentSession().createCriteria(User.class).add(Restrictions.eq("id", id))
				.uniqueResult();
		return user;
	}

}

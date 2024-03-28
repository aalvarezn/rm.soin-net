package com.soin.sgrm.dao.pos;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Authority;
import com.soin.sgrm.model.User;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.model.pos.PUser;
import com.soin.sgrm.model.pos.PUserInfo;

@Repository
public class PUserInfoDaoImpl implements PUserInfoDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PUserInfo> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PUserInfo.class);
		crit.addOrder(Order.asc("username"));
		List<PUserInfo> userInfoList = crit.list();
		return userInfoList;
	}

	@Override
	public PUserInfo getUserByUsername(String username) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PUserInfo.class);
		crit.add(Restrictions.eq("username", username));
		crit.add(Restrictions.eq("active", true));
		PUserInfo user = (PUserInfo) crit.uniqueResult();
		return user;
	}

	@Override
	public void changePassword(PUserInfo userInfo) throws SQLException {
		sessionFactory.getCurrentSession().update(userInfo);
	}

	@Override
	public PUserInfo findUserInfoById(Integer id) {
		PUserInfo user = (PUserInfo) sessionFactory.getCurrentSession().createCriteria(PUserInfo.class)
				.add(Restrictions.eq("id", id)).uniqueResult();
		return user;
	}

	@Override
	public void softDelete(PUserInfo userInfo) {
		sessionFactory.getCurrentSession().update(userInfo);
	}

	@Override
	public void updateUserInfo(PUserInfo userInfo) {
		sessionFactory.getCurrentSession().update(userInfo);
	}

	@Override
	public boolean uniqueUsername(PUserInfo userInfo) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PUserInfo.class);
		crit.add(Restrictions.eq("username", userInfo.getUsername()));
		crit.add(Restrictions.ne("id", userInfo.getId()));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return (count.intValue() == 0) ? true : false;
	}

	@Override
	public PUserInfo getUserByEmail(String email) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PUserInfo.class);
		crit.add(Restrictions.eq("emailAddress", email));
		crit.add(Restrictions.eq("active", true));
		PUserInfo user = (PUserInfo) crit.uniqueResult();
		return user;
	}

	@Override
	public void saveUserInfo(PUserInfo userInfo) {
		sessionFactory.getCurrentSession().saveOrUpdate(userInfo);
	}

	@Override
	public void delete(Integer id) {
		PUserInfo userInfo = findUserInfoById(id);
		sessionFactory.getCurrentSession().delete(userInfo);
	}

	@Override
	public PUser findUserById(Integer id) {
		PUser user = (PUser) sessionFactory.getCurrentSession().createCriteria(PUser.class).add(Restrictions.eq("id", id))
				.uniqueResult();
		return user;
	}

	@Override
	public PUserInfo getUserByGitUsername(String username) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PUserInfo.class);
		crit.add(Restrictions.eq("gitusername", username));
		crit.add(Restrictions.eq("active", true));
		PUserInfo user = (PUserInfo) crit.uniqueResult();
		return user;
	}

	@Override
	public boolean uniqueGitUsername(PUserInfo userInfo) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PUserInfo.class);
		crit.add(Restrictions.eq("gitusername", userInfo.getGitusername()));
		crit.add(Restrictions.ne("id", userInfo.getId()));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return (count.intValue() == 0) ? true : false;
	}

}

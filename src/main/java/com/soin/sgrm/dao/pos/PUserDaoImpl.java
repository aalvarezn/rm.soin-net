package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.User;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.model.pos.PUser;

@Repository
public class PUserDaoImpl implements PUserDao{
	
	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;
	
	@Override
	public boolean isValid(PUser user) {
		
		return true;
	}

	@Override
	public PUser getUserByUsername(String username) {
		PUser user = (PUser) sessionFactory.getCurrentSession().createCriteria(PUser.class)
				.add(Restrictions.eq("username", username)).uniqueResult();
		return user;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PUser> getUserRM() {
		List<PUser> users= sessionFactory.getCurrentSession().createCriteria(PUser.class)
				.add(Restrictions.eq("isReleaseManager", 1)).list();
		return users;
	}

	@Override
	public PUser findUserById(Integer id) {
		PUser user = (PUser) sessionFactory.getCurrentSession().createCriteria(PUser.class)
				.add(Restrictions.eq("id", id)).uniqueResult();
		return user;
	}


}

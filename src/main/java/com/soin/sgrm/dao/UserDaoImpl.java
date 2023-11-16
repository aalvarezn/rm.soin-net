package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.User;
import com.soin.sgrm.model.UserInfo;

@Repository
public class UserDaoImpl implements UserDao{
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public boolean isValid(User user) {
		
		return true;
	}

	@Override
	public User getUserByUsername(String username) {
		User user = (User) sessionFactory.getCurrentSession().createCriteria(User.class)
				.add(Restrictions.eq("username", username)).uniqueResult();
		return user;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUserRM() {
		List<User> users= sessionFactory.getCurrentSession().createCriteria(User.class)
				.add(Restrictions.eq("isReleaseManager", 1)).list();
		return users;
	}

	@Override
	public User findUserById(Integer id) {
		User user = (User) sessionFactory.getCurrentSession().createCriteria(User.class)
				.add(Restrictions.eq("id", id)).uniqueResult();
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public User findByName(String soinManager) {
		String [] soinManagerWords=soinManager.split(" ");
		User userReal=null;
		try { 
			List<User> users = sessionFactory.getCurrentSession()
				    .createCriteria(User.class)
				    .add(Restrictions.ilike("fullName", soinManagerWords[0].toLowerCase().substring(0,3) + "%"))
				    .list();
				
					for(User userVerification: users) {
						userReal=userVerification;
						int i=0;
						for(int x=0; x<soinManagerWords.length;x++) {
						if(x==0) {
							if(userVerification.getFullName().toLowerCase().contains(soinManagerWords[x].toLowerCase().substring(0,3))) {
								i++;
							}
						}else {
							if(userVerification.getFullName().toLowerCase().contains(soinManagerWords[x].toLowerCase())) {
								i++;
							}
						}

						if(i>=2) {
							return userReal;
						}
						}
						
						
					}
				return userReal;
				
			
		    

		    } catch (Exception e) {
		    // Otros tipos de excepciones que puedan ocurrir
		    e.printStackTrace();
		    return userReal;
		}

	}


}

package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.SystemEnvironment;

@Repository
public class SystemEnvironmentDaoImpl implements SystemEnvironmentDao {

	@Autowired
	SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SystemEnvironment> listBySystem(Integer id) {
		List<SystemEnvironment> list = sessionFactory.getCurrentSession().createCriteria(SystemEnvironment.class)
				.add(Restrictions.eq("systemId", id)).list();
		return list;
	}

}

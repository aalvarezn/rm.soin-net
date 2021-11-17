package com.soin.sgrm.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.ModifiedComponent;

@Repository
public class ModifiedComponentDaoImpl implements ModifiedComponentDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<ModifiedComponent> list() throws SQLException {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ModifiedComponent.class);
		List<ModifiedComponent> list = crit.list();
		return list;
	}

	@Override
	public ModifiedComponent findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ModifiedComponent.class);
		crit.add(Restrictions.eq("id", id));
		ModifiedComponent component = (ModifiedComponent) crit.uniqueResult();
		return component;
	}

}

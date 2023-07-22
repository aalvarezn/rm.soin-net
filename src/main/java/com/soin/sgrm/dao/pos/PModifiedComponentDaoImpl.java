package com.soin.sgrm.dao.pos;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PModifiedComponent;

@Repository
public class PModifiedComponentDaoImpl implements PModifiedComponentDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PModifiedComponent> list() throws SQLException {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PModifiedComponent.class);
		List<PModifiedComponent> list = crit.list();
		return list;
	}

	@Override
	public PModifiedComponent findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PModifiedComponent.class);
		crit.add(Restrictions.eq("id", id));
		PModifiedComponent component = (PModifiedComponent) crit.uniqueResult();
		return component;
	}

}

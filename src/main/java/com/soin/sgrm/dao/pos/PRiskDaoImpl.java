package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Risk;
import com.soin.sgrm.model.pos.PRisk;

@Repository
public class PRiskDaoImpl implements PRiskDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PRisk> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PRisk.class);
		return crit.list();
	}

	@Override
	public PRisk findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PRisk.class);
		crit.add(Restrictions.eq("id", id));
		return (PRisk) crit.uniqueResult();
	}

	@Override
	public void save(PRisk risk) {
		sessionFactory.getCurrentSession().save(risk);
	}

	@Override
	public void update(PRisk risk) {
		sessionFactory.getCurrentSession().update(risk);
	}

	@Override
	public void delete(Integer id) {
		PRisk risk = findById(id);
		sessionFactory.getCurrentSession().delete(risk);
	}

}

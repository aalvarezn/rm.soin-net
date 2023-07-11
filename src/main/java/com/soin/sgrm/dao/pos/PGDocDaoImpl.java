package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.GDoc;
import com.soin.sgrm.model.pos.PGDoc;

@Repository
public class PGDocDaoImpl implements PGDocDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PGDoc> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PGDoc.class);
		return crit.list();
	}

	@Override
	public PGDoc findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PGDoc.class);
		crit.add(Restrictions.eq("id", id));
		return (PGDoc) crit.uniqueResult();
	}

	@Override
	public void save(PGDoc gDoc) {
		sessionFactory.getCurrentSession().save(gDoc);
	}

	@Override
	public void update(PGDoc gDoc) {
		sessionFactory.getCurrentSession().update(gDoc);
	}

	@Override
	public void delete(Integer id) {
		PGDoc gDoc = findById(id);
		sessionFactory.getCurrentSession().delete(gDoc);
	}

}

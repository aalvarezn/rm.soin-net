package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.pos.PStatus;
import com.soin.sgrm.utils.Constant;

@Repository
public class PStatusDaoImpl implements PStatusDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PStatus> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PStatus.class);
		return crit.list();
	}

	@Override
	public PStatus findByName(String name) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PStatus.class);
		crit.add(Restrictions.eq("name", name));
		PStatus status = (PStatus) crit.uniqueResult();
		return status;
	}

	@Override
	public PStatus findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PStatus.class);
		crit.add(Restrictions.eq("id", id));
		return (PStatus) crit.uniqueResult();
	}

	@Override
	public void save(PStatus status) {
		sessionFactory.getCurrentSession().save(status);
	}

	@Override
	public void update(PStatus status) {
		sessionFactory.getCurrentSession().update(status);
	}

	@Override
	public void delete(Integer id) {
		PStatus status = findById(id);
		sessionFactory.getCurrentSession().delete(status);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PStatus> listWithOutAnul() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PStatus.class);
		crit.add(Restrictions.not(Restrictions.in("name", Constant.FILTRED)));
		return crit.list();
	}

}

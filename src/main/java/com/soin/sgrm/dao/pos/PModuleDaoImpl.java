package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Module;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.pos.PModule;

@Repository
public class PModuleDaoImpl implements PModuleDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@Override
	public PModule findBySystemId(String code) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PModule.class);
		crit.createCriteria("system").add(Restrictions.eq("code", code));
		PModule module = (PModule) crit.uniqueResult();
		return module;
	}

	@Override
	public PModule findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PModule.class);
		crit.add(Restrictions.eq("id", id));
		PModule module = (PModule) crit.uniqueResult();
		return module;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PModule> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PModule.class);
		return crit.list();
	}

	@Override
	public void save(PModule module) {
		sessionFactory.getCurrentSession().save(module);
	}

	@Override
	public void update(PModule module) {
		sessionFactory.getCurrentSession().update(module);
	}

	@Override
	public void delete(Integer id) {
		PModule module = findById(id);
		sessionFactory.getCurrentSession().delete(module);
	}

}

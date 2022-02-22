package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Module;
import com.soin.sgrm.model.ReleaseUser;

@Repository
public class ModuleDaoImpl implements ModuleDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Module findBySystemId(String code) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Module.class);
		crit.createCriteria("system").add(Restrictions.eq("code", code));
		Module module = (Module) crit.uniqueResult();
		return module;
	}

	@Override
	public Module findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Module.class);
		crit.add(Restrictions.eq("id", id));
		Module module = (Module) crit.uniqueResult();
		return module;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Module> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Module.class);
		return crit.list();
	}

	@Override
	public void save(Module module) {
		sessionFactory.getCurrentSession().save(module);
	}

	@Override
	public void update(Module module) {
		sessionFactory.getCurrentSession().update(module);
	}

	@Override
	public void delete(Integer id) {
		Module module = findById(id);
		sessionFactory.getCurrentSession().delete(module);
	}

}

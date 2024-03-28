package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.pos.PProject;

@Repository
public class PProjectDaoImpl implements PProjectDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PProject> listAll() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PProject.class);
		return crit.list();
	}

	@Override
	public PProject findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PProject.class);
		crit.add(Restrictions.eq("id", id));
		PProject project = (PProject) crit.uniqueResult();
		return project;
	}

	@Override
	public void save(PProject project) {
		sessionFactory.getCurrentSession().save(project);
	}

	@Override
	public void update(PProject project) {
		sessionFactory.getCurrentSession().update(project);
	}

	@Override
	public void delete(Integer id) {
		PProject project = findById(id);
		sessionFactory.getCurrentSession().delete(project);

	}

}

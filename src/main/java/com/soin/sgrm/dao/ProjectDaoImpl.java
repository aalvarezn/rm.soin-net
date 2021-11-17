package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.Project;

@Repository
public class ProjectDaoImpl implements ProjectDao {

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Project> listAll() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Project.class);
		return crit.list();
	}

	@Override
	public Project findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Project.class);
		crit.add(Restrictions.eq("id", id));
		Project project = (Project) crit.uniqueResult();
		return project;
	}

	@Override
	public void save(Project project) {
		sessionFactory.getCurrentSession().save(project);
	}

	@Override
	public void update(Project project) {
		sessionFactory.getCurrentSession().update(project);
	}

	@Override
	public void delete(Integer id) {
		Project project = findById(id);
		sessionFactory.getCurrentSession().delete(project);

	}

}

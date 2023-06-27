package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.pos.PDocTemplate;

@Repository
public class PDocTemplateDaoImpl implements PDocTemplateDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PDocTemplate> findBySystem(Integer id) {

		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PDocTemplate.class);
		crit.add(Restrictions.eq("system.id", id));
		List<PDocTemplate> list = crit.list();
		return list;
	}

	@Override
	public PDocTemplate findByTemplateName(String templateName) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PDocTemplate.class);
		crit.add(Restrictions.eq("templateName", templateName));
		PDocTemplate fileDoc = (PDocTemplate) crit.uniqueResult();
		return fileDoc;
	}

	@Override
	public PDocTemplate findById(Integer docId) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PDocTemplate.class);
		crit.add(Restrictions.eq("id", docId));
		PDocTemplate fileDoc = (PDocTemplate) crit.uniqueResult();
		return fileDoc;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PDocTemplate> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PDocTemplate.class);
		return crit.list();
	}

	@Override
	public void save(PDocTemplate docTemplate) {
		sessionFactory.getCurrentSession().save(docTemplate);
	}

	@Override
	public void update(PDocTemplate docTemplate) {
		sessionFactory.getCurrentSession().update(docTemplate);
	}

	@Override
	public void delete(Integer id) {
		PDocTemplate docTemplate = findById(id);
		sessionFactory.getCurrentSession().delete(docTemplate);
	}

}

package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.DocTemplate;

@Repository
public class DocTemplateDaoImpl implements DocTemplateDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<DocTemplate> findBySystem(Integer id) {

		Criteria crit = sessionFactory.getCurrentSession().createCriteria(DocTemplate.class);
		crit.add(Restrictions.eq("system.id", id));
		List<DocTemplate> list = crit.list();
		return list;
	}

	@Override
	public DocTemplate findByTemplateName(String templateName) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(DocTemplate.class);
		crit.add(Restrictions.eq("templateName", templateName));
		DocTemplate fileDoc = (DocTemplate) crit.uniqueResult();
		return fileDoc;
	}

	@Override
	public DocTemplate findById(Integer docId) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(DocTemplate.class);
		crit.add(Restrictions.eq("id", docId));
		DocTemplate fileDoc = (DocTemplate) crit.uniqueResult();
		return fileDoc;
	}

	@Override
	public List<DocTemplate> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(DocTemplate.class);
		return crit.list();
	}

	@Override
	public void save(DocTemplate docTemplate) {
		sessionFactory.getCurrentSession().save(docTemplate);
	}

	@Override
	public void update(DocTemplate docTemplate) {
		sessionFactory.getCurrentSession().update(docTemplate);
	}

	@Override
	public void delete(Integer id) {
		DocTemplate docTemplate = findById(id);
		sessionFactory.getCurrentSession().delete(docTemplate);
	}

}

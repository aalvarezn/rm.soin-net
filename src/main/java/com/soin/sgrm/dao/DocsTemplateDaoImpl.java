package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.DocsTemplate;

@Repository
public class DocsTemplateDaoImpl implements DocsTemplateDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<DocsTemplate> findBySystem(Integer id) {

		Criteria crit = sessionFactory.getCurrentSession().createCriteria(DocsTemplate.class);
		crit.add(Restrictions.eq("system_id", id));
		List<DocsTemplate> list = crit.list();
		return list;
	}

	@Override
	public DocsTemplate findByTemplateName(String templateName) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(DocsTemplate.class);
		crit.add(Restrictions.eq("templateName", templateName));
		DocsTemplate fileDoc = (DocsTemplate) crit.uniqueResult();
		return fileDoc;
	}

	@Override
	public DocsTemplate findById(Integer docId) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(DocsTemplate.class);
		crit.add(Restrictions.eq("id", docId));
		DocsTemplate fileDoc = (DocsTemplate) crit.uniqueResult();
		return fileDoc;
	}

}

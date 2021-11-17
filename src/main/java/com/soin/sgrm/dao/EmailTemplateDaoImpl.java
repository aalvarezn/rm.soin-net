package com.soin.sgrm.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.EmailTemplate;

@Repository
public class EmailTemplateDaoImpl implements EmailTemplateDao {

	@Autowired
	SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<EmailTemplate> listAll() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(EmailTemplate.class);
		return crit.list();
	}

	@Override
	public EmailTemplate findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(EmailTemplate.class);
		crit.add(Restrictions.eq("id", id));
		return (EmailTemplate) crit.uniqueResult();
	}

	@Override
	public void updateEmail(EmailTemplate email) {
		sessionFactory.getCurrentSession().update(email);
	}

	@Override
	public void saveEmail(EmailTemplate email) {
		sessionFactory.getCurrentSession().save(email);
	}

	@Override
	public boolean existEmailTemplate(String name) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(EmailTemplate.class);
		crit.add(Restrictions.eq("name", name));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return (count.intValue() > 0) ? true : false;
	}

	@Override
	public boolean existEmailTemplate(String name, Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(EmailTemplate.class);
		crit.add(Restrictions.eq("name", name));
		crit.add(Restrictions.ne("id", id));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return (count.intValue() > 0) ? true : false;
	}

	@Override
	public void deleteEmail(Integer id) {

		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();

			EmailTemplate email = findById(id);

			sql = String.format("DELETE FROM SISTEMA_CORREO WHERE CORREO_ID = %s ", id);
			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();

			sessionObj.delete(email);

			transObj.commit();
		} catch (Exception e) {
			e.printStackTrace();
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}

	}

}

package com.soin.sgrm.dao.pos;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.dao.AbstractDao;
import com.soin.sgrm.exception.Sentry;

import com.soin.sgrm.model.pos.PEmailTemplate;

@Repository
public class PEmailTemplateDaoImpl extends AbstractDao<Long, PEmailTemplate> implements PEmailTemplateDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;;

	@SuppressWarnings("unchecked")
	@Override
	public List<PEmailTemplate> listAll() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PEmailTemplate.class);
		return crit.list();
	}

	@Override
	public PEmailTemplate findById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PEmailTemplate.class);
		crit.add(Restrictions.eq("id", id));
		return (PEmailTemplate) crit.uniqueResult();
	}

	@Override
	public void updateEmail(PEmailTemplate email) {
		sessionFactory.getCurrentSession().update(email);
	}

	@Override
	public void saveEmail(PEmailTemplate email) {
		sessionFactory.getCurrentSession().save(email);
	}

	@Override
	public boolean existEmailTemplate(String name) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PEmailTemplate.class);
		crit.add(Restrictions.eq("name", name));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return (count.intValue() > 0) ? true : false;
	}

	@Override
	public boolean existEmailTemplate(String name, Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PEmailTemplate.class);
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

			PEmailTemplate email = findById(id);

			sql = String.format("DELETE FROM \"SISTEMA_CORREO\" WHERE \"CORREO_ID\" = %s ", id);
			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();

			sessionObj.delete(email);

			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "email");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}

	}

}

package com.soin.sgrm.dao.pos;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Request;
import com.soin.sgrm.model.TypeRequest;
import com.soin.sgrm.model.corp.RMReleaseFile;
import com.soin.sgrm.model.pos.PRequest;
import com.soin.sgrm.model.pos.PTypeRequest;

@Repository
public class PRequestDaoImpl implements PRequestDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PRequest> list(String search, Object[] projects) throws SQLException {
		String concatObjet = "";
		for (int i = 0; i < projects.length; i++) {
			concatObjet = concatObjet + " " + projects[i] + (((i + 1) == projects.length) ? "" : ",");
		}

		if (projects.length == 0)
			concatObjet = "null";

		Session sessionObj = null;
		String sql = "";
		try {
			sessionObj = sessionFactory.openSession();
			sql = String.format(
					"select r.\"ID\" , r.\"CODIGO_SOIN\" as code_soin , r.\"CODIGO_ICE\" as code_ice, r.\"DESCRIPCION\" as description from \"REQUERIMIENTOS_REQUERIMIENTO\" r "
							+ "where r.\"CODIGO_SOIN\" || ' ' ||r.\"CODIGO_ICE\" || ' ' || r.\"DESCRIPCION\" like %s "
							+ "and r.\"PROYECTO_ID\" in ( %s ) and r.\"ACTIVO\" = true LIMIT 50 ",
					"'%" + search + "%'", concatObjet);

			SQLQuery query = (SQLQuery) sessionObj.createSQLQuery(sql).addScalar("id", StandardBasicTypes.INTEGER)
					.addScalar("code_soin", StandardBasicTypes.STRING).addScalar("code_ice", StandardBasicTypes.STRING)
					.addScalar("description", StandardBasicTypes.STRING)
					.setResultTransformer(Transformers.aliasToBean(PRequest.class));
			List<PRequest> items = query.list();
			return items;

		} catch (Exception e) {
			Sentry.capture(e, "releaseRequest");
		} finally {
			sessionObj.close();
		}
		return null;
	}
	@Override
	public Integer idRequeriment(String codeSoin,String codeICE) throws SQLException {


		Session sessionObj = null;
		String sql = "";
		try {
			sessionObj = sessionFactory.openSession();
			sql = String.format(
					"select rr.\"ID\"  from \"REQUERIMIENTOS_REQUERIMIENTO\" rr  where rr.\"CODIGO_SOIN\"='%s' and rr.\"CODIGO_ICE\" = '%s'; ",
					"'%" + codeSoin + "%'", codeICE);

			SQLQuery query = (SQLQuery) sessionObj.createSQLQuery(sql)
					.addScalar("codeSoin", StandardBasicTypes.STRING)
					.addScalar("codeICE", StandardBasicTypes.STRING)
					.setResultTransformer(Transformers.aliasToBean(Request.class));
			Integer id = (Integer) query.uniqueResult();
			return id;

		} catch (Exception e) {
			Sentry.capture(e, "idRequest");
		} finally {
			sessionObj.close();
		}
		return null;
	}

	@Override
	public PRequest findById(Integer id) {
		return (PRequest) sessionFactory.getCurrentSession().get(PRequest.class, id);
	}

	@Override
	public PRequest findByName(String code_soin, String code_ice) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PRequest.class);
		crit.add(Restrictions.eqProperty("code_soin", code_soin));
		crit.add(Restrictions.eqProperty("code_ice", code_ice));
		return (PRequest) crit.uniqueResult();
	}

	@Override
	public PRequest findByName(String code_soin) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PRequest.class);
		crit.add(Restrictions.eqProperty("code_soin", code_soin));
		return (PRequest) crit.uniqueResult();
	}

	@Override
	public PRequest findByNameCode(String code_soin) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PRequest.class);
		crit.add(Restrictions.eq("code_soin", code_soin));
		return (PRequest) crit.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PRequest> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PRequest.class);
		return crit.list();
	}

	@Override
	public void save(PRequest request) {
		sessionFactory.getCurrentSession().save(request);
	}

	@Override
	public void update(PRequest request) {
		sessionFactory.getCurrentSession().update(request);
	}

	@Override
	public void delete(Integer id) {
		PRequest request = findById(id);
		sessionFactory.getCurrentSession().delete(request);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PRequest> listByType(PTypeRequest type) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PRequest.class);
		crit.createCriteria("typeRequest").add(Restrictions.eq("id", type.getId()));
		return crit.list();
	}

	@Override
	public void softDelete(PRequest request) {
		sessionFactory.getCurrentSession().update(request);
	}

	@Override
	public PRequest listByTypeAndCodeSoin(PTypeRequest type, String code_soin) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PRequest.class);
		crit.createCriteria("typeRequest").add(Restrictions.eq("id", type.getId()));
		crit.add(Restrictions.eq("code_soin", code_soin));
		return (PRequest) crit.uniqueResult();
	}

}

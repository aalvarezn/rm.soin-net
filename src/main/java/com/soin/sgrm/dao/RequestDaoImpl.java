package com.soin.sgrm.dao;

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
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Request;
import com.soin.sgrm.model.TypeRequest;
import com.soin.sgrm.model.corp.RMReleaseFile;

@Repository
public class RequestDaoImpl implements RequestDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Request> list(String search, Object[] projects) throws SQLException {
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
					"select r.id , r.codigo_soin as code_soin , r.codigo_ice as code_ice, r.descripcion as description from requerimientos_requerimiento r "
							+ "where r.codigo_soin || ' ' ||r.codigo_ice || ' ' || r.descripcion like %s "
							+ "and r.proyecto_id in ( %s ) and r.activo = 1 and ROWNUM <= 50 ",
					"'%" + search + "%'", concatObjet);

			SQLQuery query = (SQLQuery) sessionObj.createSQLQuery(sql).addScalar("id", StandardBasicTypes.INTEGER)
					.addScalar("code_soin", StandardBasicTypes.STRING).addScalar("code_ice", StandardBasicTypes.STRING)
					.addScalar("description", StandardBasicTypes.STRING)
					.setResultTransformer(Transformers.aliasToBean(Request.class));
			List<Request> items = query.list();
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
					"select rr.\"ID\"  from public.\"REQUERIMIENTOS_REQUERIMIENTO\" rr  where rr.\"CODIGO_SOIN\"='%s' and rr.\"CODIGO_ICE\" = '%s'; ",
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
	public Request findById(Integer id) {
		return (Request) sessionFactory.getCurrentSession().get(Request.class, id);
	}

	@Override
	public Request findByName(String code_soin, String code_ice) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Request.class);
		crit.add(Restrictions.eqProperty("code_soin", code_soin));
		crit.add(Restrictions.eqProperty("code_ice", code_ice));
		return (Request) crit.uniqueResult();
	}

	@Override
	public Request findByName(String code_soin) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Request.class);
		crit.add(Restrictions.eqProperty("code_soin", code_soin));
		return (Request) crit.uniqueResult();
	}

	@Override
	public Request findByNameCode(String code_soin) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Request.class);
		crit.add(Restrictions.eq("code_soin", code_soin));
		return (Request) crit.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Request> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Request.class);
		return crit.list();
	}

	@Override
	public void save(Request request) {
		sessionFactory.getCurrentSession().save(request);
	}

	@Override
	public void update(Request request) {
		sessionFactory.getCurrentSession().update(request);
	}

	@Override
	public void delete(Integer id) {
		Request request = findById(id);
		sessionFactory.getCurrentSession().delete(request);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Request> listByType(TypeRequest type) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Request.class);
		crit.createCriteria("typeRequest").add(Restrictions.eq("id", type.getId()));
		return crit.list();
	}

	@Override
	public void softDelete(Request request) {
		sessionFactory.getCurrentSession().update(request);
	}

	@Override
	public Request listByTypeAndCodeSoin(TypeRequest type, String code_soin) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(Request.class);
		crit.createCriteria("typeRequest").add(Restrictions.eq("id", type.getId()));
		crit.add(Restrictions.eq("code_soin", code_soin));
		return (Request) crit.uniqueResult();
	}

}

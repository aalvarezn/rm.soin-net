package com.soin.sgrm.dao.pos.wf;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.pos.wf.PWFRFC;
import com.soin.sgrm.utils.JsonSheet;

@Repository
public class PWFRFCDaoImpl implements PWFRFCDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PWFRFC> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PWFRFC.class);
		List<PWFRFC> userList = crit.list();
		return userList;
	}

	@Override
	public PWFRFC findWFRFCById(Long id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PWFRFC.class);
		crit.add(Restrictions.eq("id", id));
		return (PWFRFC) crit.uniqueResult();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public JsonSheet<?> listWorkFlowRFC(String name, int sEcho, int iDisplayStart, int iDisplayLength,
			String sSearch, String[] filtred, String[] dateRange, Integer systemId, Long statusId)
			throws SQLException, ParseException {

		JsonSheet json = new JsonSheet();
		Criteria crit = criteriaByWorkFlow(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, null,true);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaByWorkFlow(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, null,false);

		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<PWFRFC> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}

	@SuppressWarnings({ "deprecation" })
	public Criteria criteriaByWorkFlow(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Long statusId, Object[] ids,boolean count)
			throws SQLException, ParseException {
		List<String> fetchs=new ArrayList<String>();
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PWFRFC.class);
		crit.createAlias("systemInfo", "systemInfo");
		crit.createAlias("status", "status");
		crit.createAlias("user", "user");
		crit.createAlias("node", "node");
		crit.createAlias("node.workFlow", "workFlow");
		crit.createAlias("node.workFlow.type", "type");
		if (filtred != null) {
			crit.add(Restrictions.not(Restrictions.in("status.name", filtred)));
		}

		if (ids != null)
			crit.add(Restrictions.in("systemInfo.id", ids));

		// Valores de busqueda en la tabla
		crit.add(Restrictions.or(Restrictions.like("numRequest", sSearch, MatchMode.ANYWHERE).ignoreCase(),
				Restrictions.like("status.name", sSearch, MatchMode.ANYWHERE).ignoreCase(),
				Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase(),
				Restrictions.like("systemInfo.code", sSearch, MatchMode.ANYWHERE).ignoreCase()));
		if (dateRange != null) {
			if (dateRange.length > 1) {
				Date start = new SimpleDateFormat("dd/MM/yyyy").parse(dateRange[0]);
				Date end = new SimpleDateFormat("dd/MM/yyyy").parse(dateRange[1]);
				end.setHours(23);
				end.setMinutes(59);
				end.setSeconds(59);
				crit.add(Restrictions.ge("requestDate", start));
				crit.add(Restrictions.le("requestDate", end));
			}
		}
		if (systemId != 0) {
			crit.add(Restrictions.eq("systemInfo.id", systemId));
		}
		if (statusId != 0) {
			crit.add(Restrictions.eq("status.id", statusId));
		}
		fetchs.add("node");
		fetchs.add("workFlow");
		fetchs.add("type");
		fetchs.add("systemInfo");
		fetchs.add("status");
		fetchs.add("user");
		if (fetchs != null)
			for (String itemModel : fetchs)
				crit.setFetchMode(itemModel, FetchMode.SELECT);
		
		crit.add(Restrictions.eq("type.id", 3));
		crit.add(Restrictions.isNotNull("node"));
		if(count) {
			crit.addOrder(Order.desc("requestDate"));
		}
	
		
		return crit;
	}

	@Override
	public void wfStatusRFC(PWFRFC rfc) {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sql = String.format(
					"update rfc set id_estado = %s , nodo_id = %s , operador = '%s' , motivo = '%s' , fechasolicitud = sysdate  where id = %s",
					rfc.getStatus().getId(), rfc.getNode().getId(), rfc.getOperator(),
					rfc.getNode().getStatus().getReason(), rfc.getId());
			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();

			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public JsonSheet<?> listWorkFlowManager(String name, int sEcho, int iDisplayStart, int iDisplayLength,
			String sSearch, String[] filtred, String[] dateRange, Integer systemId, Long statusId,
			Object[] systemsId) throws SQLException, ParseException {
		JsonSheet json = new JsonSheet();
		Criteria crit = criteriaByWorkFlow(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, systemsId,true);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaByWorkFlow(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, systemsId,false);

		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<PWFRFC> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}

	@Override
	public Integer countByType(String group, Object[] ids) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PWFRFC.class);
		crit.createAlias("systemInfo", "systemInfo");
		crit.createAlias("node", "node");
		crit.createAlias("node.workFlow", "workFlow");
		crit.createAlias("workFlow.type", "type");
		crit.add(Restrictions.eq("type.id", 3));
		crit.add(Restrictions.isNotNull("node"));
		crit.add(Restrictions.in("systemInfo.id", ids));
		crit.add(Restrictions.eq("node.group", group));
		List<String> fetchs=new ArrayList<String>();
		
		fetchs.add("node");
		fetchs.add("workFlow");
		fetchs.add("type");
		fetchs.add("systemInfo");
		if (fetchs != null)
			for (String itemModel : fetchs)
				crit.setFetchMode(itemModel, FetchMode.SELECT);
		
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return count.intValue();
	}

	@Override
	public void wfStatusRFCWithOutMin(PWFRFC rfc) {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "ID";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sql = String.format(
					"update \"RFC\" set \"ESTADO_ID\" = %s , \"NODO_ID\" = %s , \"OPERADOR\" = '%s' , \"MOTIVO\" = '%s' , \"FECHA_CREACION\" = CURRENT_TIMESTAMP-(1/1440*1)  where \"ID\" = %s",
					rfc.getStatus().getId(), rfc.getNode().getId(), rfc.getOperator(),
					rfc.getNode().getStatus().getReason(), rfc.getId());
			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();

			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			transObj.rollback();
			throw e;
		} finally {
			sessionObj.close();
		}
	}
}
package com.soin.sgrm.dao.pos.wf;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
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
import com.soin.sgrm.model.pos.wf.PWFIncidence;
import com.soin.sgrm.model.wf.WFRelease;
import com.soin.sgrm.utils.JsonSheet;

@Repository
public class PWFIncidenceDaoImpl implements PWFIncidenceDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<PWFIncidence> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PWFIncidence.class);
		List<PWFIncidence> userList = crit.list();
		return userList;
	}

	@Override
	public PWFIncidence findWFIncidenceById(Long id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PWFIncidence.class);
		crit.add(Restrictions.eq("id", id));
		return (PWFIncidence) crit.uniqueResult();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public JsonSheet<?> listWorkFlowIncidence(String name, int sEcho, int iDisplayStart, int iDisplayLength,
			String sSearch, String[] filtred, String[] dateRange, Integer systemId, Long statusId)
			throws SQLException, ParseException {

		JsonSheet json = new JsonSheet();
		Criteria crit = criteriaByWorkFlow(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, null);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaByWorkFlow(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, null);

		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<WFRelease> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}

	@SuppressWarnings({ "deprecation" })
	public Criteria criteriaByWorkFlow(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Long statusId, Object[] ids)
			throws SQLException, ParseException {

		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PWFIncidence.class);
		crit.createAlias("system", "system");
		crit.createAlias("status", "status");
		crit.createAlias("user", "user");
		crit.createAlias("node", "node");
		crit.createAlias("node.workFlow", "workFlow");
		crit.createAlias("node.workFlow.type", "type");
		if (filtred != null) {
			crit.add(Restrictions.not(Restrictions.in("status.name", filtred)));
		}

		if (ids != null)
			crit.add(Restrictions.in("system.id", ids));

		// Valores de busqueda en la tabla
		crit.add(Restrictions.or(Restrictions.like("releaseNumber", sSearch, MatchMode.ANYWHERE).ignoreCase(),
				Restrictions.like("status.name", sSearch, MatchMode.ANYWHERE).ignoreCase(),
				Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase(),
				Restrictions.like("system.code", sSearch, MatchMode.ANYWHERE).ignoreCase()));
		if (dateRange != null) {
			if (dateRange.length > 1) {
				Date start = new SimpleDateFormat("dd/MM/yyyy").parse(dateRange[0]);
				Date end = new SimpleDateFormat("dd/MM/yyyy").parse(dateRange[1]);
				end.setHours(23);
				end.setMinutes(59);
				end.setSeconds(59);
				crit.add(Restrictions.ge("createDate", start));
				crit.add(Restrictions.le("createDate", end));
			}
		}
		if (systemId != 0) {
			crit.add(Restrictions.eq("system.id", systemId));
		}
		if (statusId != 0) {
			crit.add(Restrictions.eq("status.id", statusId));
		}
		crit.add(Restrictions.eq("type.id", 2));
		crit.add(Restrictions.isNotNull("node"));
		crit.addOrder(Order.desc("createDate"));
		
		return crit;
	}

	@Override
	public void wfStatusIncidence(PWFIncidence incidence) {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sql = String.format(
					"update \"INCIDENCIA\" set \"ESTADO_ID\" = %s , \"NODO_ID\" = %s , \"OPERADOR\" = '%s' , \"MOTIVO\" = '%s' , \"FECHA_CREACION\" = CURRENT_TIMESTAMP  where \"ID\" = %s",
					incidence.getStatus().getId(), incidence.getNode().getId(), incidence.getOperator(),
					incidence.getNode().getStatus().getStatus().getReason(), incidence.getId());
			query = sessionObj.createSQLQuery(sql);
			query.executeUpdate();

			transObj.commit();
		} catch (Exception e) {
			Sentry.capture(e, "release");
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
				systemId, statusId, systemsId);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaByWorkFlow(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, systemsId);

		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<WFRelease> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}

	@Override
	public Integer countByType(String group, Object[] ids) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(WFRelease.class);
		crit.createAlias("system", "system");
		crit.createAlias("node", "node");
		crit.createAlias("node.workFlow", "workFlow");
		crit.createAlias("node.workFlow.type", "type");
		crit.add(Restrictions.eq("workFlow.id", 2));
		crit.add(Restrictions.isNotNull("node"));
		crit.add(Restrictions.in("system.id", ids));
		crit.add(Restrictions.eq("node.group", group));

		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return count.intValue();
	}
}
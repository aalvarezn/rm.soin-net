package com.soin.sgrm.dao.wf;

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
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.RequestFast;
import com.soin.sgrm.model.wf.WFRelease;
import com.soin.sgrm.utils.JsonSheet;

@Repository
public class WFReleaseDaoImpl implements WFReleaseDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<WFRelease> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(WFRelease.class);
		List<WFRelease> userList = crit.list();
		return userList;
	}

	@Override
	public WFRelease findWFReleaseById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(WFRelease.class);
		crit.add(Restrictions.eq("id", id));
		return (WFRelease) crit.uniqueResult();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public JsonSheet<?> listWorkFlowRelease(String name, int sEcho, int iDisplayStart, int iDisplayLength,
			String sSearch, String[] filtred, String[] dateRange, Integer systemId, Integer statusId)
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
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId, Object[] ids)
			throws SQLException, ParseException {
		List<String> fetchs = new ArrayList<String>();
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(WFRelease.class);
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
		fetchs.add("node");
		fetchs.add("workFlow");
		fetchs.add("type");
		fetchs.add("system");
		fetchs.add("status");
		fetchs.add("user");
		if (fetchs != null)
			for (String itemModel : fetchs)
				crit.setFetchMode(itemModel, FetchMode.SELECT);

		crit.add(Restrictions.eq("type.id", 1));
		crit.add(Restrictions.isNotNull("node"));
		crit.addOrder(Order.desc("createDate"));

		return crit;
	}

	@Override
	public void wfStatusRelease(WFRelease release) {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sql = String.format(
					"update releases_release set estado_id = %s , nodo_id = %s , operador = '%s' , motivo = '%s' , fecha_creacion = sysdate  where id = %s",
					release.getStatus().getId(), release.getNode().getId(), release.getOperator(),
					release.getNode().getStatus().getMotive(), release.getId());
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
			String sSearch, String[] filtred, String[] dateRange, Integer systemId, Integer statusId,
			Object[] systemsId, Integer userId) throws SQLException, ParseException {
		JsonSheet json = new JsonSheet();
		Criteria crit = criteriaByWorkFlow(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, systemsId, userId);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaByWorkFlow(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, systemsId,userId);

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

	@SuppressWarnings("unchecked")
	private Criteria criteriaByWorkFlow(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId, Object[] ids, Integer userId)
			throws ParseException {
		List<String> fetchs = new ArrayList<String>();
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(WFRelease.class);
		Criteria crit2 = sessionFactory.getCurrentSession().createCriteria(RequestFast.class);
		crit2.add(Restrictions.eq("userManager", userId));
		List<RequestFast> requestList = crit2.list();
		Disjunction disjunction = Restrictions.disjunction();
		// Valores de busqueda en la tabla
		if(sSearch.equals("")) {
		for (RequestFast request : requestList) {
			String codeSoing=request.getCode_soin().replaceFirst("-", "");
			if (!codeSoing.equals("")) {
				disjunction.add(Restrictions.like("releaseNumber", codeSoing, MatchMode.ANYWHERE).ignoreCase());
		    }
			
		}
		}
		
		if(requestList.size()!=0 || !sSearch.equals("")) {
			crit.createAlias("system", "system");
			crit.createAlias("status", "status");
			crit.createAlias("user", "user");
			crit.createAlias("node", "node");
			crit.createAlias("node.workFlow", "workFlow");
			crit.createAlias("node.workFlow.type", "type");
			if (ids != null)
				crit.add(Restrictions.in("system.id", ids));
			
			
			
			if (filtred != null) {
				crit.add(Restrictions.not(Restrictions.in("status.name", filtred)));
			}
			if(sSearch.equals("")) {
				crit.add(disjunction);
				crit.add(Restrictions.or(
						Restrictions.like("status.name", sSearch, MatchMode.ANYWHERE).ignoreCase(),
						Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase(),
						Restrictions.like("system.code", sSearch, MatchMode.ANYWHERE).ignoreCase()));
			}else {
				crit.add(Restrictions.or(
						Restrictions.like("status.name", sSearch, MatchMode.ANYWHERE).ignoreCase(),
						Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase(),
						Restrictions.like("system.code", sSearch, MatchMode.ANYWHERE).ignoreCase(),
						Restrictions.like("releaseNumber", sSearch, MatchMode.ANYWHERE).ignoreCase()));
			}
			
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
			fetchs.add("node");
			fetchs.add("workFlow");
			fetchs.add("type");
			fetchs.add("system");
			fetchs.add("status");
			fetchs.add("user");
			if (fetchs != null)
				for (String itemModel : fetchs)
					crit.setFetchMode(itemModel, FetchMode.SELECT);

			crit.add(Restrictions.eq("type.id", 1));
			crit.add(Restrictions.isNotNull("node"));
			crit.addOrder(Order.desc("createDate"));
		}else {
			crit.add(Restrictions.eq("id", null));
		}
		

		return crit;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer countByType(String group, Object[] ids,Integer userId) {
		Criteria crit2 = sessionFactory.getCurrentSession().createCriteria(RequestFast.class);
		crit2.add(Restrictions.eq("userManager", userId));
		List<RequestFast> requestList = crit2.list();
		Disjunction disjunction = Restrictions.disjunction();
		// Valores de busqueda en la tabla
		for (RequestFast request : requestList) {
			String codeSoing=request.getCode_soin().replaceFirst("-", "");
			if (!codeSoing.equals("")) {
				disjunction.add(Restrictions.like("releaseNumber", codeSoing, MatchMode.ANYWHERE).ignoreCase());
		    }
			
		}
	
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(WFRelease.class);
		if(requestList.size()!=0) {
		crit.createAlias("system", "system");
		crit.createAlias("node", "node");
		crit.createAlias("node.workFlow", "workFlow");
		crit.createAlias("workFlow.type", "type");
		crit.add(disjunction);
		crit.add(Restrictions.eq("type.id", 1));
		crit.add(Restrictions.isNotNull("node"));
		crit.add(Restrictions.in("system.id", ids));
		crit.add(Restrictions.eq("node.group", group));
		List<String> fetchs = new ArrayList<String>();

		fetchs.add("node");
		fetchs.add("workFlow");
		fetchs.add("type");
		fetchs.add("system");
		if (fetchs != null)
			for (String itemModel : fetchs)
				crit.setFetchMode(itemModel, FetchMode.SELECT);

	
		}else {
			crit.add(Restrictions.eq("id", null));
		}
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		
		return count.intValue();
	}

	@Override
	public void wfStatusReleaseWithOutMin(WFRelease release) {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sql = String.format(
					"update releases_release set estado_id = %s , nodo_id = %s , operador = '%s' , motivo = '%s' , fecha_creacion = sysdate-(1/1440*1)  where id = %s",
					release.getStatus().getId(), release.getNode().getId(), release.getOperator(),
					release.getNode().getStatus().getMotive(), release.getId());
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
}
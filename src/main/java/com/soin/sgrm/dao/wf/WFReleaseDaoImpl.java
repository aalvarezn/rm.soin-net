package com.soin.sgrm.dao.wf;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

import io.sentry.event.User;

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
	@Override
	public void updateReleaseNode(WFRelease releaseEdit) throws Exception {
		Transaction transObj = null;
		Session sessionObj = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sessionObj.update(releaseEdit);

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
			List<Integer> listIdRelease, Integer userId,Object[] systemIds) throws SQLException, ParseException {
		JsonSheet json = new JsonSheet();
		Criteria crit = criteriaByWorkFlow(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, listIdRelease, userId,systemIds);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaByWorkFlow(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, listIdRelease,userId,systemIds);

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
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId,List<Integer> listIdRelease, Integer userId,Object[] systemIds)
			throws ParseException {
		List<String> fetchs = new ArrayList<String>();
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(WFRelease.class);

		
	
			crit.createAlias("system", "system");
			crit.createAlias("status", "status");
			crit.createAlias("user", "user");
			crit.createAlias("node", "node");
			crit.createAlias("node.workFlow", "workFlow");
			crit.createAlias("node.workFlow.type", "type");
			List<Integer> listIdSystem=new ArrayList<Integer>();
			if(!listIdRelease.isEmpty()) {
			crit.add(Restrictions.in("id",listIdRelease));
			}else {
				
				for(Object system: systemIds ) {
					listIdSystem.add((Integer) system);
				}
			}
			if (filtred != null) {
				crit.add(Restrictions.not(Restrictions.in("status.name", filtred)));
			}
			if(sSearch.equals("")) {
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
			}else {
				if(listIdRelease.isEmpty()) {
					crit.add(Restrictions.in("system.id", listIdSystem));
				}
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
	public Integer countByType(String group,List<Integer> listIdRelease,Integer userId,Object[] systemIds) {
	

			Criteria crit = sessionFactory.getCurrentSession().createCriteria(WFRelease.class);
			crit.createAlias("system", "system");
			crit.createAlias("node", "node");
			crit.createAlias("node.workFlow", "workFlow");
			crit.createAlias("workFlow.type", "type");
			crit.add(Restrictions.eq("type.id", 1));
			crit.add(Restrictions.isNotNull("node"));
			List<Integer> listIdSystem=new ArrayList<Integer>();
			for(Object system: systemIds ) {
				listIdSystem.add((Integer) system);
			}
			if(!listIdRelease.isEmpty()) {
				crit.add(Restrictions.in("id", listIdRelease));
			}else {
				crit.add(Restrictions.in("system.id", listIdSystem));
			}
			
			crit.add(Restrictions.eq("node.group", group));
			List<String> fetchs = new ArrayList<String>();

			fetchs.add("node");
			fetchs.add("workFlow");
			fetchs.add("type");
			fetchs.add("system");
			if (fetchs != null)
				for (String itemModel : fetchs)
					crit.setFetchMode(itemModel, FetchMode.SELECT);
			
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
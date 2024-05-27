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
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.RequestFast;
import com.soin.sgrm.model.pos.PRequestFast;
import com.soin.sgrm.model.pos.wf.PWFRelease;
import com.soin.sgrm.model.wf.WFRelease;
import com.soin.sgrm.utils.JsonSheet;

@Repository
public class PWFReleaseDaoImpl implements PWFReleaseDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;
	@SuppressWarnings("unchecked")
	@Override
	public List<PWFRelease> list() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PWFRelease.class);
		List<PWFRelease> userList = crit.list();
		return userList;
	}

	@Override
	public PWFRelease findWFReleaseById(Integer id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PWFRelease.class);
		crit.add(Restrictions.eq("id", id));
		return (PWFRelease) crit.uniqueResult();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public JsonSheet<?> listWorkFlowRelease(String name, int sEcho, int iDisplayStart, int iDisplayLength,
			String sSearch, String[] filtred, String[] dateRange, Integer systemId, Integer statusId)
			throws SQLException, ParseException {

		JsonSheet json = new JsonSheet();
		Criteria crit = criteriaByWorkFlow(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, null,false,null);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaByWorkFlow(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, null,true,null);

		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<PWFRelease> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public Criteria criteriaByWorkFlow(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId, Object[] ids,boolean count, Integer idUser)
			throws SQLException, ParseException {
		List<String> fetchs = new ArrayList<String>();
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PWFRelease.class);
		Criteria crit2 = sessionFactory.getCurrentSession().createCriteria(PRequestFast.class);
		crit2.add(Restrictions.eq("userManager", idUser));
		List<PRequestFast> requestList = crit2.list();
		Disjunction disjunction = Restrictions.disjunction();
		// Valores de busqueda en la tabla
		if(sSearch.equals("")) {
		for (PRequestFast request : requestList) {
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
			//crit.add(Restrictions.eq("id", null));
			
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
			if(!count) {
				crit.addOrder(Order.desc("createDate"));
			}
			
			
		}
		return crit;
	}

	@Override
	public void wfStatusRelease(PWFRelease release) {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sql = String.format(
					"update \"RELEASES_RELEASE\" set \"ESTADO_ID\" = %s , \"NODO_ID\" = %s , \"OPERADOR\" = '%s' , \"MOTIVO\" = '%s' , \"FECHA_CREACION\" = CURRENT_TIMESTAMP  where \"ID\" = %s",
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
			Object[] systemsId,Integer idUser) throws SQLException, ParseException {
		JsonSheet json = new JsonSheet();
		Criteria crit = criteriaByWorkFlow(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, systemsId,false,idUser);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaByWorkFlow(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, systemsId,true,idUser);

		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<PWFRelease> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}

	@Override
	public Integer countByType(String group, Object[] ids,Integer idUser) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PWFRelease.class);
		crit.createAlias("system", "system");
		crit.createAlias("node", "node");
		crit.createAlias("node.workFlow", "workFlow");
		crit.createAlias("workFlow.type", "type");
		crit.add(Restrictions.eq("type.id", 1));
		crit.add(Restrictions.isNotNull("node"));
		crit.add(Restrictions.in("system.id", ids));
		crit.add(Restrictions.eq("node.group", group));
		List<String> fetchs=new ArrayList<String>();
		
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
	public void wfStatusReleaseWithOutMin(PWFRelease release) {
		Transaction transObj = null;
		Session sessionObj = null;
		String sql = "";
		Query query = null;
		try {
			sessionObj = sessionFactory.openSession();
			transObj = sessionObj.beginTransaction();
			sql = String.format(
					"update \"RELEASES_RELEASE\" set \"ESTADO_ID\" = %s , \"NODO_ID\" = %s , \"OPERADOR\" = '%s' , \"MOTIVO\" = '%s' , \"FECHA_CREACION\" = CURRENT_TIMESTAMP-(1/1440*1)  where \"ID\" = %s",
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
package com.soin.sgrm.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.RFCReport;
import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.RequestBaseR1;
import com.soin.sgrm.model.RequestBaseTrackingShow;
import com.soin.sgrm.model.RequestRM_P1_R1;
import com.soin.sgrm.model.RequestReport;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.utils.JsonSheet;

@Repository
public class RequestBaseR1DaoImpl extends AbstractDao<Long, RequestBaseR1> implements RequestBaseR1Dao{
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private SystemService systemService;

	@SuppressWarnings("unchecked")
	@Override
	public Integer countByType(Integer id, String type, int query, Object[] ids) {
		Criteria crit = getSession().createCriteria(RequestBaseR1.class);
		crit.createAlias("user", "user");
		crit.createAlias("status", "status");
		switch (query) {
		case 1:
			// query #1 Obtiene mis rfc
			List<SystemInfo> systems = sessionFactory.getCurrentSession().createCriteria(SystemInfo.class)
			.createAlias("managers","managers")
			.add(Restrictions.eq("managers.id", id)).list();
			List<Integer> listaId=new ArrayList<Integer>();
			for(SystemInfo system: systems) {
				listaId.add(system.getId());
			}
			crit.createAlias("systemInfo", "systemInfo");
			crit.add(Restrictions.in("systemInfo.id", listaId));

			
			crit.add(Restrictions.eq("status.name", type));
			break;
		case 2:
			crit.add(Restrictions.eq("status.name", type));
		
		default:
			break;
		}
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return count.intValue();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Integer countByManager(Integer id, Long idRequest) {
		Criteria crit = getSession().createCriteria(RequestBaseR1.class);
		crit.createAlias("user", "user");
		crit.createAlias("status", "status");

			// query #1 Obtiene mis request
			List<SystemInfo> systems = sessionFactory.getCurrentSession().createCriteria(SystemInfo.class)
			.createAlias("managers","managers")
			.add(Restrictions.eq("managers.id", id)).list();
			List<Integer> listaId=new ArrayList<Integer>();
			for(SystemInfo system: systems) {
				listaId.add(system.getId());
			}
			crit.createAlias("systemInfo", "systemInfo");
			crit.add(Restrictions.in("systemInfo.id", listaId));
			crit.add(Restrictions.eq("id", idRequest));

		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return count.intValue();
	}
	@Override
	public RequestBaseR1 getByIdR1(Long id) {
	    return (RequestBaseR1) getSession().createCriteria(RequestBaseR1.class)
	    		.add(Restrictions.eq("id", id))
	    		.uniqueResult();
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public JsonSheet<?> findAllReportRequest(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			String dateRange, Integer systemId, Long typePetitionId) throws ParseException {
		JsonSheet json = new JsonSheet();
		Criteria crit = criteriaByReport( sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange,
				systemId,typePetitionId);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaByReport( sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange,
				systemId,typePetitionId);

		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<RFCReport> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}


	private Criteria criteriaByReport(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			String dateRange, Integer systemId, Long typePetitionId) throws ParseException {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(RequestReport.class);
		crit.createAlias("system", "system");
		crit.createAlias("user", "user");
		crit.createAlias("typePetition", "typePetition");
		

		

		// Valores de busqueda en la tabla
		if (sSearch != null && !((sSearch.trim()).equals("")))
			crit.add(Restrictions.or(Restrictions.like("description", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("requestNumber", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("system.code", sSearch, MatchMode.ANYWHERE).ignoreCase()));
		String[] range = (dateRange != null) ? dateRange.split("-") : null;
		if (range != null) {
			if (range.length > 1) {
				
				
				Date start = new SimpleDateFormat("dd/MM/yyyy").parse(range[0]);
				Date end = new SimpleDateFormat("dd/MM/yyyy").parse(range[1]);
				end.setHours(23);
				end.setMinutes(59);
				end.setSeconds(59);

				crit.add(Restrictions.ge("requestDate", start));
				crit.add(Restrictions.le("requestDate", end));
			}
		}
		if (systemId != 0) {
			crit.add(Restrictions.eq("system.id", systemId));
		}
		
		if (typePetitionId != 0) {
			crit.add(Restrictions.eq("typePetition.id", typePetitionId));
		}
		
		

		crit.addOrder(Order.desc("requestDate"));

		return crit;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public JsonSheet<?> findAllReportRequest(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			String dateRange, List<Integer> systemsId, Long typePetitionId) throws ParseException {
		JsonSheet json = new JsonSheet();
		Criteria crit = criteriaByReport( sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange,
				systemsId,typePetitionId);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaByReport( sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange,
				systemsId,typePetitionId);

		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<RFCReport> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}


	private Criteria criteriaByReport(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			String dateRange, List<Integer> systemsId, Long typePetitionId) throws ParseException {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(RequestReport.class);
		crit.createAlias("system", "system");
		crit.createAlias("user", "user");
		crit.createAlias("siges", "siges");
		crit.createAlias("typePetition", "typePetition");

		

		// Valores de busqueda en la tabla
		if (sSearch != null && !((sSearch.trim()).equals("")))
			crit.add(Restrictions.or(Restrictions.like("description", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("requestNumber", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("system.code", sSearch, MatchMode.ANYWHERE).ignoreCase()));
		String[] range = (dateRange != null) ? dateRange.split("-") : null;
		if (range != null) {
			if (range.length > 1) {
				
				
				Date start = new SimpleDateFormat("dd/MM/yyyy").parse(range[0]);
				Date end = new SimpleDateFormat("dd/MM/yyyy").parse(range[1]);
				end.setHours(23);
				end.setMinutes(59);
				end.setSeconds(59);

				crit.add(Restrictions.ge("requestDate", start));
				crit.add(Restrictions.le("requestDate", end));
			}
		}
		if (systemsId != null) {
			crit.add(Restrictions.in("system.id", systemsId));
		}
		
		if (typePetitionId != 0) {
			crit.add(Restrictions.eq("typePetition.id", typePetitionId));
		}
		
		

		crit.addOrder(Order.desc("requestDate"));

		return crit;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<RequestReport> listRequestReportFilter(int projectId, int systemId, Long typePetitionId,
			String dateRange) throws ParseException {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(RequestReport.class);
		crit.createAlias("system", "system");
		crit.createAlias("typePetition", "typePetition");
		crit.createAlias("user", "user");

	


		String[] range = (dateRange != null) ? dateRange.split("-") : null;
		if (range != null) {
			if (range.length > 1) {
				
				
				Date start = new SimpleDateFormat("dd/MM/yyyy").parse(range[0]);
				Date end = new SimpleDateFormat("dd/MM/yyyy").parse(range[1]);
				end.setHours(23);
				end.setMinutes(59);
				end.setSeconds(59);

				crit.add(Restrictions.ge("requestDate", start));
				crit.add(Restrictions.le("requestDate", end));
			}
		}
		
		
		if (systemId != 0) {
			crit.add(Restrictions.eq("system.id", systemId));
		}else {
			if(projectId!=0) {
				List<System> systemList=systemService.getSystemByProject(projectId);
				crit.add(Restrictions.in("system.id", systemList));
			}
			
		}
		if (typePetitionId != 0) {
			crit.add(Restrictions.eq("typePetition.id", typePetitionId));
		}

		crit.addOrder(Order.desc("requestDate"));

		return crit.list();
	}


	@Override
	public RequestBaseTrackingShow findRequestTracking(Long id) {
		return (RequestBaseTrackingShow) getSession().createCriteria(RequestBaseTrackingShow.class)
	    		.add(Restrictions.eq("id", id))
	    		.uniqueResult();
	}
	
	
}

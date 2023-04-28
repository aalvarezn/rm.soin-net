package com.soin.sgrm.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.RFCReport;
import com.soin.sgrm.model.RFCReportComplete;
import com.soin.sgrm.model.RFCTrackingShow;
import com.soin.sgrm.model.RFCTrackingToError;
import com.soin.sgrm.model.RFC_WithoutRelease;
import com.soin.sgrm.model.ReleaseReportFast;
import com.soin.sgrm.model.ReleaseTrackingToError;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.Releases_WithoutObj;
import com.soin.sgrm.model.System;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.service.SystemService;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.JsonSheet;

@Repository
public class RFCDaoImpl extends AbstractDao<Long,RFC> implements RFCDao {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private SystemService systemService;
	
	@Override
	public Integer existNumRFC(String numRequest) {
		Criteria crit = getSession().createCriteria(RFC.class);
		crit.add(Restrictions.like("numRequest", numRequest, MatchMode.ANYWHERE));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		int recordsTotal = count.intValue();
		return recordsTotal;

	}

	@Override
	public void updateStatusRFC(RFC rfc, String dateChange) throws Exception {
		String sql = "";
		Query query = null;
		try {
			

			String dateChangeUpdate = (dateChange != null && !dateChange.equals("")
					? "to_date('" + dateChange + "', 'DD-MM-YYYY HH:MI PM')"
					: "sysdate");

			sql = String.format(
					"update sgrm.\"RFC\" set \"ID_ESTADO\" = %s , \"OPERADOR\" = '%s' , \"MOTIVO\" = '%s' , \"FECHASOLICITUD\" = "
							+ dateChangeUpdate + "  where \"ID\" = %s",
					rfc.getStatus().getId(), rfc.getOperator(), rfc.getMotive(),
					rfc.getId());

			query = getSession().createSQLQuery(sql);
			query.executeUpdate();

			
		} catch (Exception e) {
			Sentry.capture(e, "rfc");
			
			throw e;
		} 
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer countByType(Integer id, String type, int query, Object[] ids) {
		Criteria crit = getSession().createCriteria(RFC.class);
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
	public Integer countByManager(Integer id, Long idRFC) {
		Criteria crit = getSession().createCriteria(RFC.class);
		crit.createAlias("user", "user");
		crit.createAlias("status", "status");

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
			crit.add(Restrictions.eq("id", idRFC));

		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return count.intValue();
	}

	@Override
	public RFC_WithoutRelease findRfcWithRelease(Long id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(RFC_WithoutRelease.class);
		crit.add(Restrictions.eq("id", id));
		RFC_WithoutRelease rfc = (RFC_WithoutRelease) crit.uniqueResult();
		return rfc;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RFCTrackingToError> listByAllSystemError(String dateRange, int systemId) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(RFCTrackingToError.class);
		crit.createAlias("rfc", "rfc");
		crit.createAlias("rfc.system", "system");
		String[] range = (dateRange != null) ? dateRange.split("-") : null;
		if (range != null) {
			if (range.length > 1) {
				try {
					Date start = new SimpleDateFormat("dd/MM/yyyy").parse(range[0]);
					Date end = new SimpleDateFormat("dd/MM/yyyy").parse(range[1]);
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);
					crit.add(Restrictions.ge("trackingDate", start));
					crit.add(Restrictions.le("trackingDate", end));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		if (systemId != 0) {
			crit.add(Restrictions.eq("system.id", systemId));
		}
		crit.add(Restrictions.eq("status", "Solicitado"));
		crit.addOrder(Order.desc("trackingDate"));

		return crit.list();
	}

	@Override
	public RFCTrackingShow findRFCTracking(Long id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(RFCTrackingShow.class);
		crit.add(Restrictions.eq("id", id));
		RFCTrackingShow rfc = (RFCTrackingShow) crit.uniqueResult();
		return rfc;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public JsonSheet<?> findAllReportRFC( Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, String dateRange, int systemId,Long sigesId) throws ParseException, HibernateException {
		JsonSheet json = new JsonSheet();
		Criteria crit = criteriaByReport( sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange,
				systemId,sigesId);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaByReport( sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange,
				systemId,sigesId);

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

	private Criteria criteriaByReport( Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, String dateRange, int systemId,Long sigesId) throws ParseException {
	
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(RFCReport.class);
		crit.createAlias("system", "system");
		crit.createAlias("siges", "siges");
		crit.createAlias("user", "user");

	

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
		if (sigesId != 0) {
			crit.add(Restrictions.eq("siges.id", sigesId));
		}

		crit.addOrder(Order.desc("requestDate"));

		return crit;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public JsonSheet<?> findAllReportRFC(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			String dateRange, List<Integer> systemsId, Long sigesId) throws ParseException {
		JsonSheet json = new JsonSheet();
		Criteria crit = criteriaByReport( sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange,
				systemsId,sigesId);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaByReport( sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange,
				systemsId,sigesId);

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
			String dateRange, List<Integer> systemsId, Long sigesId) throws ParseException {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(RFCReport.class);
		crit.createAlias("system", "system");
		crit.createAlias("user", "user");
		crit.createAlias("siges", "siges");
		

		

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
		
		if (sigesId != 0) {
			crit.add(Restrictions.eq("siges.id", sigesId));
		}
		
		

		crit.addOrder(Order.desc("requestDate"));

		return crit;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<RFCReport> listRFCReportFilter(int projectId, int systemId, Long sigesId, String dateRange) throws ParseException {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(RFCReport.class);
		crit.createAlias("system", "system");
		crit.createAlias("siges", "siges");
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
		if (sigesId != 0) {
			crit.add(Restrictions.eq("siges.id", sigesId));
		}

		crit.addOrder(Order.desc("requestDate"));

		return crit.list();
	}

	@Override
	public RFCReportComplete findByIdRFCReport(Long id) {
		RFCReportComplete rfc = (RFCReportComplete) sessionFactory.getCurrentSession().createCriteria(RFCReportComplete.class)
				.add(Restrictions.eq("id", id)).uniqueResult();
		return rfc;
	
	}
	
	
	
	

}
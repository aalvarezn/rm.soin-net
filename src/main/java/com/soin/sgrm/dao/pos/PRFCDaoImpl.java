package com.soin.sgrm.dao.pos;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.pos.PRFC;
import com.soin.sgrm.model.pos.PRFCReport;
import com.soin.sgrm.model.pos.PRFCReportComplete;
import com.soin.sgrm.model.pos.PRFCTrackingShow;
import com.soin.sgrm.model.pos.PRFCTrackingToError;
import com.soin.sgrm.model.pos.PRFC_WithoutRelease;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.service.pos.PSystemService;
import com.soin.sgrm.utils.JsonSheet;

@Repository
public class PRFCDaoImpl extends AbstractDao<Long,PRFC> implements PRFCDao {

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@Autowired
	private PSystemService systemService;
	
	@Override
	public Integer existNumRFC(String numRequest) {
		Criteria crit = getSession().createCriteria(PRFC.class);
		crit.add(Restrictions.like("numRequest", numRequest, MatchMode.ANYWHERE));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		int recordsTotal = count.intValue();
		return recordsTotal;

	}

	@Override
	public void updateStatusRFC(PRFC rfc, String dateChange) throws Exception {
		String sql = "";
		Query query = null;
		try {
			

			String dateChangeUpdate = (dateChange != null && !dateChange.equals("")
					? "to_date('" + dateChange + "', 'DD-MM-YYYY HH:MI PM')"
					: "sysdate");

			sql = String.format(
					"update \"RFC\" set \"ID_ESTADO\" = %s , \"OPERADOR\" = '%s' , \"MOTIVO\" = '%s' , \"FECHASOLICITUD\" = "
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
		Criteria crit = getSession().createCriteria(PRFC.class);
		crit.createAlias("user", "user");
		crit.createAlias("status", "status");
		switch (query) {
		case 1:
			// query #1 Obtiene mis rfc
			List<PSystemInfo> systems = sessionFactory.getCurrentSession().createCriteria(PSystemInfo.class)
			.createAlias("managers","managers")
			.add(Restrictions.eq("managers.id", id)).list();
			List<Integer> listaId=new ArrayList<Integer>();
			for(PSystemInfo system: systems) {
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
		Criteria crit = getSession().createCriteria(PRFC.class);
		crit.createAlias("user", "user");
		crit.createAlias("status", "status");

			// query #1 Obtiene mis rfc
			List<PSystemInfo> systems = sessionFactory.getCurrentSession().createCriteria(PSystemInfo.class)
			.createAlias("managers","managers")
			.add(Restrictions.eq("managers.id", id)).list();
			List<Integer> listaId=new ArrayList<Integer>();
			for(PSystemInfo system: systems) {
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
	public PRFC_WithoutRelease findRfcWithRelease(Long id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PRFC_WithoutRelease.class);
		crit.add(Restrictions.eq("id", id));
		PRFC_WithoutRelease rfc = (PRFC_WithoutRelease) crit.uniqueResult();
		return rfc;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<PRFCTrackingToError> listByAllSystemError(String dateRange, int systemId) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PRFCTrackingToError.class);
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
	public PRFCTrackingShow findRFCTracking(Long id) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PRFCTrackingShow.class);
		crit.add(Restrictions.eq("id", id));
		PRFCTrackingShow rfc = (PRFCTrackingShow) crit.uniqueResult();
		return rfc;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public JsonSheet<?> findAllReportRFC( Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, String dateRange, int systemId,Long sigesId) throws ParseException, HibernateException {
		JsonSheet json = new JsonSheet();
		Criteria crit = criteriaByReport( sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange,
				systemId,sigesId,false);

		crit.setFirstResult(iDisplayStart);
		crit.setMaxResults(iDisplayLength);

		Criteria critCount = criteriaByReport( sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange,
				systemId,sigesId,true);

		critCount.setProjection(Projections.rowCount());
		Long count = (Long) critCount.uniqueResult();
		int recordsTotal = count.intValue();

		List<PRFCReport> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
		
	}

	@SuppressWarnings("deprecation")
	private Criteria criteriaByReport( Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, String dateRange, int systemId,Long sigesId,boolean count) throws ParseException {
	
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PRFCReport.class);
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
		if(!count) {
			crit.addOrder(Order.desc("requestDate"));
			
		}
		

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

		List<PRFCReport> aaData = crit.list();
		json.setDraw(sEcho);
		json.setRecordsTotal(recordsTotal);
		json.setRecordsFiltered(recordsTotal);
		json.setData(aaData);
		return json;
	}

	@SuppressWarnings("deprecation")
	private Criteria criteriaByReport(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			String dateRange, List<Integer> systemsId, Long sigesId) throws ParseException {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PRFCReport.class);
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
	public List<PRFCReport> listRFCReportFilter(int projectId, int systemId, Long sigesId, String dateRange) throws ParseException {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PRFCReport.class);
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
				List<PSystem> systemList=systemService.getSystemByProject(projectId);
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
	public PRFCReportComplete findByIdRFCReport(Long id) {
		PRFCReportComplete rfc = (PRFCReportComplete) sessionFactory.getCurrentSession().createCriteria(PRFCReportComplete.class)
				.add(Restrictions.eq("id", id)).uniqueResult();
		return rfc;
	
	}
	
	
	
	

}
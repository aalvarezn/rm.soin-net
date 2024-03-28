package com.soin.sgrm.service.pos;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.RFCDao;
import com.soin.sgrm.dao.pos.PRFCDao;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.RFC;
import com.soin.sgrm.model.RFCReport;
import com.soin.sgrm.model.RFCReportComplete;
import com.soin.sgrm.model.RFCTrackingShow;
import com.soin.sgrm.model.RFCTrackingToError;
import com.soin.sgrm.model.RFC_WithoutRelease;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.pos.PRFC;
import com.soin.sgrm.model.pos.PRFCReport;
import com.soin.sgrm.model.pos.PRFCReportComplete;
import com.soin.sgrm.model.pos.PRFCTrackingShow;
import com.soin.sgrm.model.pos.PRFCTrackingToError;
import com.soin.sgrm.model.pos.PRFC_WithoutRelease;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.response.JsonSheet;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.Constant;
import com.soin.sgrm.utils.MyLevel;

@Service("PRFCService")
@Transactional("transactionManagerPos")
public class PRFCServiceImpl implements PRFCService {

	@Autowired
	PRFCDao dao;

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	public static final Logger logger = Logger.getLogger(PRFCServiceImpl.class);

	@Override
	public PRFC findByKey(String name, String value) {
		return null;
	}

	@Override
	public List<PRFC> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PRFC model) {
		dao.save(model);

	}

	@Override
	public void delete(Long id) {
		PRFC model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PRFC model) {
		dao.update(model);

	}

	@Override
	public PRFC findById(Long id) {

		return dao.getById(id);
	}

	@SuppressWarnings("deprecation")
	@Override
	public JsonSheet<PRFC> findAll1(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long sStatus, String dateRange, int sPriority, int systemId) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();

		alias.put("status", "status");
		alias.put("user", "user");
		alias.put("typeChange", "typeChange");
		String[] range = (dateRange != null) ? dateRange.split("-") : null;
		if (range != null) {
			if (range.length > 1) {
				try {
					Date start = new SimpleDateFormat("dd/MM/yyyy").parse(range[0]);
					Date end = new SimpleDateFormat("dd/MM/yyyy").parse(range[1]);
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);
					columns.put("requestDate", Restrictions.between("requestDate", start, end));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		Criterion qSrch = null;
		if (sSearch != null && sSearch.length() > 0) {
			qSrch = Restrictions.or(Restrictions.like("numRequest", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("reasonChange", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("typeChange.name", sSearch, MatchMode.ANYWHERE).ignoreCase()
					);
		}
		if (sStatus == 0) {
			sStatus = null;
		}
		if (sStatus != null) {

			columns.put("status", Restrictions.eq("status.id", sStatus));
		}

		if (sPriority != 0) {
			alias.put("priority", "priority");
			columns.put("priority", Restrictions.eq("priority.id", sPriority));
		}
		if (systemId != 0) {
			alias.put("siges", "siges");
			columns.put("siges", Restrictions.or(Restrictions.eq("siges.system.id", systemId)));

		}

		List<String> fetchs = new ArrayList<String>();

		return dao.findAll(sEcho, iDisplayStart, iDisplayLength, columns, qSrch, fetchs, alias, 1);
	}

	public String verifySecuence(String partCode) {
		String numRFC = "";
		try {
			int amount = existNumRFC(partCode);

			if (amount == 0) {
				numRFC = partCode + "_01_" + CommonUtils.getSystemDate("yyyyMMdd");
				return numRFC;
			} else {
				if (amount < 10) {
					numRFC = partCode + "_0" + (amount + 1) + "_" + CommonUtils.getSystemDate("yyyyMMdd");
					return numRFC;
				}
				numRFC = partCode + "_" + (amount + 1) + "_" + CommonUtils.getSystemDate("yyyyMMdd");
				return numRFC;
			}

		} catch (Exception e) {
			Sentry.capture(e, "release");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return "Sin Asignar";
	}

	public String generateRFCNumber(String codeProject,String codeSystem) {
		String numRFC = "";
		String partCode = "";
		try {

			partCode = "RFC_" + codeProject.toUpperCase() +"_"+codeSystem.toUpperCase()+ "_SC";

			numRFC = verifySecuence(partCode);

		} catch (Exception e) {
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			numRFC = "Sin Asignar";
		}
		return numRFC;
	}

	@Override
	public Integer existNumRFC(String number_release) throws SQLException {
		return dao.existNumRFC(number_release);
	}

	@Override
	public Integer countByType(Integer id, String type, int query, Object[] ids) {
		// TODO Auto-generated method stub
		return dao.countByType(id, type, query, ids);
	}

	@Override
	public Integer countByManager(Integer id, Long idRFC) {
		return dao.countByManager(id, idRFC);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public JsonSheet<PRFC> findAll2(Integer id, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long sStatus, String dateRange, int sPriority, int systemId) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();

		List<PSystemInfo> systems = sessionFactory.getCurrentSession().createCriteria(PSystemInfo.class)
				.createAlias("managers", "managers").add(Restrictions.eq("managers.id", id)).list();
		alias.put("status", "status");
		alias.put("typeChange", "typeChange");
		String[] range = (dateRange != null) ? dateRange.split("-") : null;
		if (range != null) {
			if (range.length > 1) {
				try {
					Date start = new SimpleDateFormat("dd/MM/yyyy").parse(range[0]);
					Date end = new SimpleDateFormat("dd/MM/yyyy").parse(range[1]);
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);
					columns.put("requestDate", Restrictions.between("requestDate", start, end));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		Criterion qSrch = null;
		if (sSearch != null && sSearch.length() > 0) {
			alias.put("user", "user");
			qSrch = Restrictions.or(

					Restrictions.like("numRequest", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("reasonChange", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("typeChange.name", sSearch, MatchMode.ANYWHERE).ignoreCase()

			);
		}
		if (sStatus == 0) {
			sStatus = null;
		}

		if (sStatus != null) {

			columns.put("status", Restrictions.eq("status.id", sStatus));
		} else {
			columns.put("status", Restrictions.not(Restrictions.in("status.name", Constant.FILTREDRFC)));
		}

		if (sPriority != 0) {
			alias.put("priority", "priority");
			columns.put("priority", Restrictions.eq("priority.id", sPriority));
		}
		if (systemId != 0) {
			alias.put("siges", "siges");
			columns.put("siges", Restrictions.or(Restrictions.eq("siges.system.id", systemId)));

		} else {
			List<Integer> listaId = new ArrayList<Integer>();
			for (PSystemInfo system : systems) {
				listaId.add(system.getId());
			}
			alias.put("systemInfo", "systemInfo");
			columns.put("systemInfo", (Restrictions.in("systemInfo.id", listaId)));
		}

		List<String> fetchs = new ArrayList<String>();
		fetchs.add("releases");
		fetchs.add("files");
		fetchs.add("tracking");
		fetchs.add("user");
		return dao.findAll(sEcho, iDisplayStart, iDisplayLength, columns, qSrch, fetchs, alias, 1);
	}
	@Override
	public PRFC_WithoutRelease findRfcWithRelease(Long id) {
		
		return dao.findRfcWithRelease(id);
	}

	@Override
	public List<PRFCTrackingToError> listByAllSystemError(String dateRange, int systemId) {

				return dao.listByAllSystemError(dateRange, systemId);
	}

	@Override
	public PRFCTrackingShow findRFCTracking(Long id) {
		
		return dao.findRFCTracking(id);
	}


	@Override
	public com.soin.sgrm.utils.JsonSheet<?> findAllReportRFC( Integer sEcho, Integer iDisplayStart,
			Integer iDisplayLength, String sSearch, String dateRange, int systemId,Long sigesId) throws ParseException {
		return dao.findAllReportRFC( sEcho, iDisplayStart,iDisplayLength, sSearch, dateRange,systemId,sigesId);
	}



	@Override
	public com.soin.sgrm.utils.JsonSheet<?> findAllReportRFC(Integer sEcho, Integer iDisplayStart,
			Integer iDisplayLength, String sSearch, String dateRange, List<Integer> systemsId,Long sigesId) throws ParseException {
		return dao.findAllReportRFC( sEcho, iDisplayStart,iDisplayLength, sSearch, dateRange,systemsId,sigesId);
	}

	@Override
	public List<PRFCReport> listRFCReportFilter(int projectId, int systemId, Long sigesId, String dateRange) throws ParseException {
		
		return dao.listRFCReportFilter(projectId, systemId, sigesId, dateRange);
	}

	@Override
	public PRFCReportComplete findByIdRFCReport(Long id) {
		return dao.findByIdRFCReport(id);
	}

}
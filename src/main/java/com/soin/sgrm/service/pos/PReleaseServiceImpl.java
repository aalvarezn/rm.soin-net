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
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PReleaseDao;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.model.pos.PReleaseEdit;
import com.soin.sgrm.model.pos.PReleaseEditWithOutObjects;
import com.soin.sgrm.model.pos.PReleaseObjectEdit;
import com.soin.sgrm.model.pos.PReleaseReport;
import com.soin.sgrm.model.pos.PReleaseReportFast;
import com.soin.sgrm.model.pos.PReleaseSummary;
import com.soin.sgrm.model.pos.PReleaseSummaryFile;
import com.soin.sgrm.model.pos.PReleaseSummaryMin;
import com.soin.sgrm.model.pos.PReleaseTinySummary;
import com.soin.sgrm.model.pos.PReleaseTrackingShow;
import com.soin.sgrm.model.pos.PReleaseTrackingToError;
import com.soin.sgrm.model.pos.PReleaseUser;
import com.soin.sgrm.model.pos.PReleaseUserFast;
import com.soin.sgrm.model.pos.PRelease_RFC;
import com.soin.sgrm.model.pos.PRelease_RFCFast;
import com.soin.sgrm.model.pos.PReleases_WithoutObj;
import com.soin.sgrm.model.pos.PRequest;
import com.soin.sgrm.model.pos.PUserInfo;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonSheet;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.utils.ReleaseCreate;

@Transactional("transactionManagerPos")
@Service("PReleaseService")
public class PReleaseServiceImpl implements PReleaseService {

	public static final Logger logger = Logger.getLogger(PReleaseServiceImpl.class);

	@Autowired
	private PRequestService requestService;

	@Autowired
	PReleaseDao dao;

	@Override
	public PReleaseSummary findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public Integer countByType(String name, String type, int query, Object[] ids) {
		return dao.countByType(name, type, query, ids);
	}

	@Override
	public JsonSheet<?> listByUser(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] dateRange, Integer systemId, Integer statusId) throws SQLException, ParseException {
		return dao.listByUser(name, sEcho, iDisplayStart, iDisplayLength, sSearch, dateRange, systemId, statusId);
	}

	@Override
	public JsonSheet<?> listByTeams(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			Object[] ids, String[] dateRange, Integer systemId, Integer statusId) throws SQLException, ParseException {
		return dao.listByTeams(name, sEcho, iDisplayStart, iDisplayLength, sSearch, ids, dateRange, systemId, statusId);
	}

	@Override
	public JsonSheet<?> listByAllSystem(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId)
			throws SQLException, ParseException {
		return dao.listByAllSystem(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange, systemId,
				statusId);
	}

	@Override
	public JsonSheet<?> listByAllWithObjects(String name, int sEcho, int iDisplayStart, int iDisplayLength,
			String sSearch, String[] filtred, String[] dateRange, Integer systemId, Integer statusId, Integer projectId)
			throws SQLException, ParseException {
		return dao.listByAllWithObjects(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, projectId);
	}

	@Override
	public JsonSheet<?> listByAllSystemQA(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId)
			throws SQLException, ParseException {
		return dao.listByAllSystemQA(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange, systemId,
				statusId);
	}

	@Override
	public Integer existNumRelease(String number_release) throws SQLException {
		return dao.existNumRelease(number_release);
	}

	@Override
	public PReleaseEdit findEditById(Integer id) throws SQLException {
		return dao.findEditById(id);
	}

	@Override
	public void save(PRelease release, String tpos) throws Exception {
		dao.save(release, tpos);

	}

	@Override
	public List<PReleaseUser> list(String search, String release_id) throws SQLException {
		return dao.list(search, release_id);
	}

	@Override
	public void updateStatusRelease(PReleaseEdit release) throws Exception {
		dao.updateStatusRelease(release);
	}

	@Override
	public PRelease_RFC findRelease_RFCById(Integer id) throws SQLException {
		return dao.findRelease_RFCById(id);
	}

	@Override
	public void saveRelease(PRelease release, ReleaseCreate rc) throws Exception {
		dao.saveRelease(release, rc);
	}

	@Override
	public void requestRelease(PRelease release) throws SQLException {
		dao.requestRelease(release);
	}

	@Override
	public ArrayList<PReleaseObjectEdit> saveReleaseObjects(Integer release_id, ArrayList<PReleaseObjectEdit> objects) {
		return dao.saveReleaseObjects(release_id, objects);
	}

	@Override
	public void copy(PReleaseEdit release, String tpos) throws Exception {
		dao.copy(release, tpos);
	}

	@Override
	public void assignRelease(PReleaseEdit release, PUserInfo user) throws SQLException {
		dao.assignRelease(release, user);
	}

	@Override
	public PReleaseUser findReleaseUserById(Integer id) throws SQLException {
		return dao.findReleaseUserById(id);
	}

	public String generateReleaseNumber(String requeriment, String requirement_name, String system_id) {
		String number_release = "";
		String partCode = "";
		try {

			switch (requeriment) {
			case "IN":
				// Si no comienza con IN, se agrega.
				if (!requirement_name.substring(0, 2).toString().toUpperCase().equals("IN")) {
					partCode = system_id + "." + "IN" + requirement_name;
				} else {
					partCode = system_id + "." + requirement_name;
				}
				number_release = verifySecuence(partCode);
				break;
			case "PR":
				// Si no comienza con PR, se agrega.
				if (!requirement_name.substring(0, 2).toString().toUpperCase().equals("PR")) {
					partCode = system_id + "." + "PR" + requirement_name;
				} else {
					partCode = system_id + "." + requirement_name;
				}
				number_release = verifySecuence(partCode);
				break;
			case "SS":
				// Si no comienza con SS, se agrega.
				if (!requirement_name.substring(0, 2).toString().toUpperCase().equals("SS")) {
					partCode = system_id + "." + "SS" + requirement_name;
				} else {
					partCode = system_id + "." + requirement_name;
				}
				number_release = verifySecuence(partCode);
				break;
			case "SO-ICE":
				partCode = system_id + "." + "SO-ICE" + requirement_name;
				number_release = verifySecuence(partCode);
				break;
			case "INFRA":
				partCode = system_id + "." + "INFRA" + "." + requirement_name;
				number_release = verifySecuence(partCode);
				break;
			default:
				number_release = "Sin Asignar";
				break;
			}
		} catch (Exception e) {
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
			number_release = "Sin Asignar";
		}
		return number_release;
	}

	public String verifySecuence(String partCode) {
		try {
			int amount = existNumRelease(partCode);

			if (amount == 0) {
				return partCode + "." + CommonUtils.getSystemDate("yyyyMMdd");
			} else {
				return partCode + "_" + (amount + 1) + "." + CommonUtils.getSystemDate("yyyyMMdd");
			}

		} catch (Exception e) {
			Sentry.capture(e, "release");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return "Sin Asignar";

	}

	public String generateTPO_BT_ReleaseNumber(String system_id, String requirement_name) {

		try {
			String ids = requirement_name;
			String[] words = ids.split(",");
			String partCode = "";
			int id = Integer.parseInt(words[0]);
			String number_release = "";
			PRequest request = requestService.findById(id);
			String[] code = request.getCode_soin().split("-");
			String codeSOIN = "";
			String codeICE = request.getCode_ice();
			for (int i = 0; i < code.length; i++) {
				if (i <= 1) {
					codeSOIN += code[i];
				} else {
					codeSOIN += "-" + code[i];
				}
			}
			// Esto es para los releases de ATV
			if (request.getCode_soin().substring(0, 2).toString().equals("R-")) {
				partCode = system_id + "." + codeSOIN;
			} else {
				// en caso de las BT el codeICE es null por lo que no es requerido.
				if (codeICE == null) {
					partCode = system_id + "." + codeSOIN;
				} else {
					partCode = system_id + "." + codeSOIN + "." + codeICE;
				}
			}
			partCode = partCode.replaceAll("\\s", ""); // Se eliminan los espacios en blanco
			number_release = verifySecuence(partCode);
			return number_release;
		} catch (Exception e) {
			Sentry.capture(e, "release");
			logger.log(MyLevel.RELEASE_ERROR, e.toString());
		}
		return "Sin Asignar";
	}

	@Override
	public JsonSheet<?> listReleasesBySystem(int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			Integer systemId) throws SQLException, ParseException {
		return dao.listReleasesBySystem(sEcho, iDisplayStart, iDisplayLength, sSearch, systemId);
	}

	@Override
	public PRelease findReleaseById(Integer id) throws SQLException {

		return dao.findReleaseById(id);
	}

	@Override
	public Integer getDependency(int id) {
		return dao.getDependency(id);
	}

	@Override
	public void updateStatusReleaseRFC(PRelease_RFCFast release, String operator) throws Exception {
		dao.updateStatusReleaseRFC(release, operator);
	}

	@Override
	public PReleases_WithoutObj findReleaseWithouObj(Integer id) throws SQLException {

		return dao.findReleaseWithouObj(id);
	}

	@Override
	public PReleaseSummaryMin findByIdMin(Integer id) throws SQLException {
		return dao.findByIdMin(id);
	}

	@Override
	public PReleaseEditWithOutObjects findEditByIdWithOutObjects(Integer idRelease) {

		return dao.findEditByIdWithOutObjects(idRelease);
	}

	@Override
	public PReleaseTinySummary findByIdTiny(int id) {
		return dao.findByIdTiny(id);
	}

	@Override
	public List<PReleaseTrackingToError> listByAllSystemError(String dateRange, int systemId) {
		// TODO Auto-generated method stub
		return dao.listByAllSystemError(dateRange, systemId);
	}

	@Override
	public PReleaseReport findByIdReleaseReport(Integer id) {
		return dao.findByIdReleaseReport(id);
	}

	@Override
	public List<PReleaseReport> listReleaseReport() {

		return dao.listReleaseReport();

	}

	@Override
	public List<PReleaseReportFast> listReleaseReportFilter(int systemId, int projectId, String dateRange) {
		return dao.listReleaseReportFilter(systemId, projectId, dateRange);
	}

	@Override
	public PRelease_RFCFast findRelease_RFCByIdFast(int id) {
		return dao.findRelease_RFCByIdFast(id);
	}

	@Override
	public PReleaseTrackingShow findReleaseTracking(int id) {

		return dao.findReleaseTracking(id);
	}

	@Override
	public PReleaseSummaryFile findByIdSummaryFile(Integer id) {
		// TODO Auto-generated method stub
		return dao.findByIdSummaryFile(id);
	}

	@Override
	public JsonSheet<?> listByAllWithOutTracking(String name, int sEcho, int iDisplayStart, int iDisplayLength,
			String sSearch, String[] filtred, String[] dateRange, Integer systemId, Integer statusId, Integer projectId)
			throws SQLException, ParseException {
		return dao.listByAllWithOutTracking(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange,
				systemId, statusId, projectId);
	}

	@Override
	public void requestRelease(PReleaseEditWithOutObjects release) {
		dao.requestRelease(release);
	}

	@Override
	public String getLastStatusHistory(Integer id) {
		// TODO Auto-generated method stub
		return dao.getLastStatusHistory(id);
	}

	@Override
	public PReleaseUserFast findByIdReleaseUserFast(Integer idRelease) {
		// TODO Auto-generated method stub
		return dao.findByIdReleaseUserFast(idRelease);
	}

	@Override
	public void updateStatusRelease(PReleaseUserFast release) {
		dao.updateStatusReleaseUser(release);
	}

	@Override
	public JsonSheet<?> findAll1(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			 String dateRange, Integer systemId, Integer statusId, boolean all)
			throws SQLException, ParseException {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();
		
		alias.put("system", "system");
		alias.put("status", "status");
		alias.put("user", "user");

		// Valores de busqueda en la tabla
		
		Criterion qSrch = null;
		if (sSearch != null && sSearch.length() > 0) {
			qSrch= Restrictions.or(Restrictions.like("description", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("releaseNumber", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("status.name", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("system.code", sSearch, MatchMode.ANYWHERE).ignoreCase());
		}
		
		String[] range = (dateRange != null) ? dateRange.split("-") : null;
		if (range != null) {
			if (range.length > 1) {
				try {
					Date start = new SimpleDateFormat("dd/MM/yyyy").parse(range[0]);
					Date end = new SimpleDateFormat("dd/MM/yyyy").parse(range[1]);
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);
					columns.put("createDate", Restrictions.between("createDate", start, end));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		
		if (systemId != 0) {
			columns.put("system", Restrictions.eq("system.id", systemId));
			
		}
		if (statusId != 0) {
			columns.put("status", Restrictions.eq("status.id", statusId));
			
		}
		
		if(!all) {
			if(name!="") {
				columns.put("user", Restrictions.eq("user.username", name));
			}
		}
	
		
		List<String> fetchs = new ArrayList<String>();

		return dao.findAllFastRelease(sEcho, iDisplayStart, iDisplayLength, columns, qSrch, fetchs, alias, 1);

	}
	
	

	@Override
	public JsonSheet<?> listByTeams(String name, int sEcho, int iDisplayStart, Integer iDisplayLength, String sSearch,
			Object[] ids, String dateRange, Integer systemId, Integer statusId) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();
		
		alias.put("system", "system");
		alias.put("status", "status");
		alias.put("user", "user");

		// Valores de busqueda en la tabla
		columns.put("ids",Restrictions.in("system.id", ids));
		Criterion qSrch = null;
		if (sSearch != null && sSearch.length() > 0) {
			qSrch= Restrictions.or(Restrictions.like("description", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("releaseNumber", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("status.name", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("system.code", sSearch, MatchMode.ANYWHERE).ignoreCase());
		}
		
		String[] range = (dateRange != null) ? dateRange.split("-") : null;
		if (range != null) {
			if (range.length > 1) {
				try {
					Date start = new SimpleDateFormat("dd/MM/yyyy").parse(range[0]);
					Date end = new SimpleDateFormat("dd/MM/yyyy").parse(range[1]);
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);
					columns.put("createDate", Restrictions.between("createDate", start, end));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		
		if (systemId != 0) {
			columns.put("system", Restrictions.eq("system.id", systemId));
			
		}
		if (statusId != 0) {
			columns.put("status", Restrictions.eq("status.id", statusId));
			
		}
		
		List<String> fetchs = new ArrayList<String>();

		return dao.findAllFastRelease(sEcho, iDisplayStart, iDisplayLength, columns, qSrch, fetchs, alias, 1);
	}

	@Override
	public JsonSheet<?> listByAllSystem(String name, int sEcho, int iDisplayStart, int iDisplayLength, String[] filtred,
			String sSearch, String dateRange, Integer systemId, Integer statusId) {
		Map<String, Object> columns = new HashMap<String, Object>();

		Map<String, String> alias = new HashMap<String, String>();
		
		alias.put("system", "system");
		alias.put("status", "status");
		alias.put("user", "user");

		// Valores de busqueda en la tabla
		if (filtred != null) {
			columns.put("status2",Restrictions.not(Restrictions.in("status.name", filtred)));
		}
		Criterion qSrch = null;
		if (sSearch != null && sSearch.length() > 0) {
			qSrch= Restrictions.or(Restrictions.like("description", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("releaseNumber", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("status.name", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("user.fullName", sSearch, MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("system.code", sSearch, MatchMode.ANYWHERE).ignoreCase());
		}
		
		String[] range = (dateRange != null) ? dateRange.split("-") : null;
		if (range != null) {
			if (range.length > 1) {
				try {
					Date start = new SimpleDateFormat("dd/MM/yyyy").parse(range[0]);
					Date end = new SimpleDateFormat("dd/MM/yyyy").parse(range[1]);
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);
					columns.put("createDate", Restrictions.between("createDate", start, end));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		
		if (systemId != 0) {
			columns.put("system", Restrictions.eq("system.id", systemId));
			
		}
		if (statusId != 0) {
			columns.put("status", Restrictions.eq("status.id", statusId));
			
		}
		
		List<String> fetchs = new ArrayList<String>();

		return dao.findAllFastRelease(sEcho, iDisplayStart, iDisplayLength, columns, qSrch, fetchs, alias, 1);
	}
	
	

}
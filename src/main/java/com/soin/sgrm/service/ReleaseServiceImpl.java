package com.soin.sgrm.service;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.controller.ReleaseController;
import com.soin.sgrm.dao.ReleaseDao;
import com.soin.sgrm.exception.Sentry;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.ReleaseEditWithOutObjects;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.ReleaseObjectEdit;
import com.soin.sgrm.model.ReleaseReport;
import com.soin.sgrm.model.ReleaseReportFast;
import com.soin.sgrm.model.Risk;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.ReleaseSummary;
import com.soin.sgrm.model.ReleaseSummaryMin;
import com.soin.sgrm.model.ReleaseTinySummary;
import com.soin.sgrm.model.ReleaseTrackingShow;
import com.soin.sgrm.model.ReleaseTrackingToError;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.Release_RFC;
import com.soin.sgrm.model.Release_RFCFast;
import com.soin.sgrm.model.Releases_WithoutObj;
import com.soin.sgrm.model.Request;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.utils.CommonUtils;
import com.soin.sgrm.utils.JsonSheet;
import com.soin.sgrm.utils.MyLevel;
import com.soin.sgrm.utils.ReleaseCreate;

@Transactional("transactionManager")
@Service("ReleaseService")
public class ReleaseServiceImpl implements ReleaseService {

	public static final Logger logger = Logger.getLogger(ReleaseServiceImpl.class);

	@Autowired
	private RequestService requestService;

	@Autowired
	ReleaseDao dao;

	@Override
	public ReleaseSummary findById(Integer id) {
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
	public JsonSheet<?> listByAllWithObjects(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId,Integer projectId)
			throws SQLException, ParseException {
		return dao.listByAllWithObjects(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange, systemId,
				statusId,projectId);
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
	public ReleaseEdit findEditById(Integer id) throws SQLException {
		return dao.findEditById(id);
	}

	@Override
	public void save(Release release, String tpos) throws Exception {
		dao.save(release, tpos);

	}

	@Override
	public List<ReleaseUser> list(String search, String release_id) throws SQLException {
		return dao.list(search, release_id);
	}

	@Override
	public void updateStatusRelease(ReleaseEdit release) throws Exception {
		dao.updateStatusRelease(release);
	}

	@Override
	public Release_RFC findRelease_RFCById(Integer id) throws SQLException {
		return dao.findRelease_RFCById(id);
	}

	@Override
	public void saveRelease(Release release, ReleaseCreate rc) throws Exception {
		dao.saveRelease(release, rc);
	}

	@Override
	public void requestRelease(Release release) throws SQLException {
		dao.requestRelease(release);
	}

	@Override
	public ArrayList<ReleaseObjectEdit> saveReleaseObjects(Integer release_id, ArrayList<ReleaseObjectEdit> objects) {
		return dao.saveReleaseObjects(release_id, objects);
	}

	@Override
	public void copy(ReleaseEdit release, String tpos) throws Exception {
		dao.copy(release, tpos);
	}

	@Override
	public void assignRelease(ReleaseEdit release, UserInfo user) throws SQLException {
		dao.assignRelease(release, user);
	}

	@Override
	public ReleaseUser findReleaseUserById(Integer id) throws SQLException {
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
			Request request = requestService.findById(id);
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
	public Release findReleaseById(Integer id) throws SQLException {

		return dao.findReleaseById(id);
	}

	@Override
	public Integer getDependency(int id) {
		return dao.getDependency(id);
	}

	@Override
	public void updateStatusReleaseRFC(Release_RFCFast release, String operator) throws Exception {
		dao.updateStatusReleaseRFC(release, operator);
	}

	@Override
	public Releases_WithoutObj findReleaseWithouObj(Integer id) throws SQLException {

		return dao.findReleaseWithouObj(id);
	}

	@Override
	public ReleaseSummaryMin findByIdMin(Integer id) throws SQLException {
		return dao.findByIdMin(id);
	}

	@Override
	public ReleaseEditWithOutObjects findEditByIdWithOutObjects(Integer idRelease) {

		return dao.findEditByIdWithOutObjects(idRelease);
	}

	@Override
	public ReleaseTinySummary findByIdTiny(int id) {
		return dao.findByIdTiny(id);
	}

	@Override
	public List<ReleaseTrackingToError> listByAllSystemError(String dateRange, int systemId) {
		// TODO Auto-generated method stub
		return dao.listByAllSystemError(dateRange, systemId);
	}

	@Override
	public ReleaseReport findByIdReleaseReport(Integer id) {
		return dao.findByIdReleaseReport(id);
	}

	@Override
	public List<ReleaseReport> listReleaseReport() {
		
		return dao.listReleaseReport();
				
	}

	@Override
	public List<ReleaseReportFast> listReleaseReportFilter(int systemId, int projectId, String dateRange) {
		return  dao.listReleaseReportFilter(systemId,projectId,dateRange);
	}

	@Override
	public Release_RFCFast findRelease_RFCByIdFast(int id) {
		return dao.findRelease_RFCByIdFast(id);
	}

	@Override
	public ReleaseTrackingShow findReleaseTracking(int id) {
	
		return dao.findReleaseTracking(id);
	}

	@Override
	public JsonSheet<?> listByAllWithOutTracking(String name, int sEcho, int iDisplayStart, int iDisplayLength,
			String sSearch, String[] filtred, String[] dateRange, Integer systemId, Integer statusId, Integer projectId)
			throws SQLException, ParseException {
		return dao.listByAllWithOutTracking(name, sEcho, iDisplayStart, iDisplayLength, sSearch, filtred, dateRange, systemId,
				statusId,projectId);
	}

}
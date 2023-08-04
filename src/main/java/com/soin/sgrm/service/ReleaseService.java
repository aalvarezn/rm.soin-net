package com.soin.sgrm.service;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

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
import com.soin.sgrm.model.ReleaseSummaryFile;
import com.soin.sgrm.model.ReleaseSummaryMin;
import com.soin.sgrm.model.ReleaseTinySummary;
import com.soin.sgrm.model.ReleaseTrackingShow;
import com.soin.sgrm.model.ReleaseTrackingToError;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.Release_RFC;
import com.soin.sgrm.model.Release_RFCFast;
import com.soin.sgrm.model.Releases_WithoutObj;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.utils.JsonSheet;
import com.soin.sgrm.utils.ReleaseCreate;

public interface ReleaseService {

	/* Creado para visualizar la paginacion por ajax de 10 items */
	JsonSheet<?> listByUser(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] dateRange, Integer systemId, Integer statusId) throws SQLException, ParseException;

	JsonSheet<?> listByTeams(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			Object[] ids, String[] dateRange, Integer systemId, Integer statusId) throws SQLException, ParseException;

	JsonSheet<?> listByAllSystem(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId)
			throws SQLException, ParseException;
	JsonSheet<?> listByAllSystemQA(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId)
			throws SQLException, ParseException;
	/* #Paginacion por ajax de 10 items# */

	Integer countByType(String name, String type, int query, Object[] ids);

	Integer existNumRelease(String number_release) throws SQLException;

	ReleaseSummary findById(Integer id) throws SQLException;

	ReleaseEdit findEditById(Integer id) throws SQLException;

	ReleaseUser findReleaseUserById(Integer id) throws SQLException;
	
	Releases_WithoutObj findReleaseWithouObj(Integer id) throws SQLException;

	void save(Release release, String tpos) throws Exception;

	void copy(ReleaseEdit release, String tpos) throws Exception;

	void assignRelease(ReleaseEdit release, UserInfo user) throws SQLException;

	List<ReleaseUser> list(String search, String release_id) throws SQLException;

	void updateStatusRelease(ReleaseEdit release) throws Exception;

	void updateStatusReleaseRFC(Release_RFCFast release,String operator) throws Exception;
	
	Release findReleaseById(Integer id) throws SQLException;

	void saveRelease(Release release, ReleaseCreate rc) throws Exception;

	void requestRelease(Release release) throws SQLException;

	ArrayList<ReleaseObjectEdit> saveReleaseObjects(Integer release_id, ArrayList<ReleaseObjectEdit> objects);

	String generateTPO_BT_ReleaseNumber(String system_id, String requirement_name);

	String verifySecuence(String partCode);
	
	String generateReleaseNumber(String requeriment, String requirement_name, String system_id);

	public JsonSheet<?> listReleasesBySystem( int sEcho, int iDisplayStart, int iDisplayLength, String sSearch, Integer systemId) throws SQLException, ParseException ;

	Release_RFC findRelease_RFCById(Integer id) throws SQLException;

	Integer getDependency(int id);
	
	ReleaseSummaryMin findByIdMin(Integer id) throws SQLException;

	ReleaseEditWithOutObjects findEditByIdWithOutObjects(Integer idRelease);

	ReleaseTinySummary findByIdTiny(int parseInt);

	List<ReleaseTrackingToError> listByAllSystemError(String dateRange, int systemId);

	ReleaseReport findByIdReleaseReport(Integer id);

	List<ReleaseReport> listReleaseReport();

	JsonSheet<?> listByAllWithObjects(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId, Integer projectId)
			throws SQLException, ParseException;

	JsonSheet<?> listByAllWithOutTracking(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId, Integer projectId)
			throws SQLException, ParseException;
	List<ReleaseReportFast> listReleaseReportFilter(int systemId, int projectId, String dateRange);

	Release_RFCFast findRelease_RFCByIdFast(int id);

	ReleaseTrackingShow findReleaseTracking(int id);

	ReleaseSummaryFile findByIdSummaryFile(Integer id);

	ReleaseEdit findEditByName(String numRelease);

	



}
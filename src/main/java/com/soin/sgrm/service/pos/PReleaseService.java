package com.soin.sgrm.service.pos;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

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
import com.soin.sgrm.model.pos.PUserInfo;
import com.soin.sgrm.utils.JsonSheet;
import com.soin.sgrm.utils.ReleaseCreate;

public interface PReleaseService {

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

	PReleaseSummary findById(Integer id) throws SQLException;

	PReleaseEdit findEditById(Integer id) throws SQLException;

	PReleaseUser findReleaseUserById(Integer id) throws SQLException;
	
	PReleases_WithoutObj findReleaseWithouObj(Integer id) throws SQLException;

	void save(PRelease release, String tpos) throws Exception;

	void copy(PReleaseEdit release, String tpos) throws Exception;

	void assignRelease(PReleaseEdit release, PUserInfo user) throws SQLException;

	List<PReleaseUser> list(String search, String release_id) throws SQLException;

	void updateStatusRelease(PReleaseEdit release) throws Exception;

	void updateStatusReleaseRFC(PRelease_RFCFast release,String operator) throws Exception;
	
	PRelease findReleaseById(Integer id) throws SQLException;

	void saveRelease(PRelease release, ReleaseCreate rc) throws Exception;

	void requestRelease(PRelease release) throws SQLException;

	ArrayList<PReleaseObjectEdit> saveReleaseObjects(Integer release_id, ArrayList<PReleaseObjectEdit> objects);

	String generateTPO_BT_ReleaseNumber(String system_id, String requirement_name);

	String verifySecuence(String partCode);
	
	String generateReleaseNumber(String requeriment, String requirement_name, String system_id);

	public JsonSheet<?> listReleasesBySystem( int sEcho, int iDisplayStart, int iDisplayLength, String sSearch, Integer systemId) throws SQLException, ParseException ;

	PRelease_RFC findRelease_RFCById(Integer id) throws SQLException;

	Integer getDependency(int id);
	
	PReleaseSummaryMin findByIdMin(Integer id) throws SQLException;

	PReleaseEditWithOutObjects findEditByIdWithOutObjects(Integer idRelease);

	PReleaseTinySummary findByIdTiny(int parseInt);

	List<PReleaseTrackingToError> listByAllSystemError(String dateRange, int systemId);

	PReleaseReport findByIdReleaseReport(Integer id);

	List<PReleaseReport> listReleaseReport();

	JsonSheet<?> listByAllWithObjects(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId, Integer projectId)
			throws SQLException, ParseException;

	JsonSheet<?> listByAllWithOutTracking(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId, Integer projectId)
			throws SQLException, ParseException;
	List<PReleaseReportFast> listReleaseReportFilter(int systemId, int projectId, String dateRange);

	PRelease_RFCFast findRelease_RFCByIdFast(int id);

	PReleaseTrackingShow findReleaseTracking(int id);

	PReleaseSummaryFile findByIdSummaryFile(Integer id);

	void requestRelease(PReleaseEditWithOutObjects release);

	String getLastStatusHistory(Integer id);

	PReleaseUserFast findByIdReleaseUserFast(Integer idRelease);

	void updateStatusRelease(PReleaseUserFast release);


	JsonSheet<?> findAll1(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			 String dateRange, Integer systemId, Integer statusId,boolean all)
			throws SQLException, ParseException;

	JsonSheet<?> listByTeams(String name, int sEcho, int iDisplayStart, Integer iDisplayLength, String sSearch,
			Object[] myTeams, String dateRange2, Integer systemId, Integer statusId);

	JsonSheet<?> listByAllSystem(String name, int sEcho, int iDisplayStart, int iDisplayLength, String[] filtred,
			String sSearch, String dateRange2, Integer systemId, Integer statusId);

	List<Integer> findByIdManager(Integer idUser);




}
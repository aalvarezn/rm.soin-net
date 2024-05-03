package com.soin.sgrm.dao.pos;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;
import org.hibernate.criterion.Criterion;

import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.model.pos.PReleaseEdit;
import com.soin.sgrm.model.pos.PReleaseEditWithOutObjects;
import com.soin.sgrm.model.pos.PReleaseError;
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

public interface PReleaseDao {
	/* Creado para visualizar la paginacion por ajax de 10 items */
	JsonSheet<?> listByUser(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] dateRange, Integer systemId, Integer statusId) throws SQLException, ParseException;

	JsonSheet<?> listByTeams(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			Object[] ids, String[] dateRange, Integer systemId, Integer statusId) throws SQLException, ParseException;

	JsonSheet<?> listByAllSystem(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId) throws SQLException, ParseException;
	
	JsonSheet<?> listByAllSystemQA(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId) throws SQLException, ParseException;
	
	/* #Paginacion por ajax de 10 items# */

	Integer countByType(String name, String type, int query, Object[] ids);

	Integer existNumRelease(String number_release);

	PReleaseSummary findById(Integer id);

	PReleaseEdit findEditById(Integer id) throws SQLException;

	PReleaseUser findReleaseUserById(Integer id) throws SQLException;

	void save(PRelease release, String tpos) throws Exception;

	void copy(PReleaseEdit release, String tpos) throws Exception;

	List<PReleaseUser> list(String search, String release_id);

	void updateStatusRelease(PReleaseEdit release) throws Exception;

	PRelease findReleaseById(Integer id);

	void saveRelease(PRelease release, ReleaseCreate rc) throws Exception;

	void assignRelease(PReleaseEdit release, PUserInfo user) throws SQLException;

	void requestRelease(PRelease release);

	ArrayList<PReleaseObjectEdit> saveReleaseObjects(Integer release_id, ArrayList<PReleaseObjectEdit> objects);

	JsonSheet<?> listReleasesBySystem(int sEcho, int iDisplayStart, int iDisplayLength, String sSearch, Integer systemId) throws SQLException, ParseException;

	PRelease_RFC findRelease_RFCById(Integer id);

	Integer getDependency(int id);

	void updateStatusReleaseRFC(PRelease_RFCFast release,String operator);
  
	PReleases_WithoutObj findReleaseWithouObj(Integer id);

	void insertReleaseError(PReleaseError release) throws Exception;

	PReleaseSummaryMin findByIdMin(Integer id);

	PReleaseEditWithOutObjects findEditByIdWithOutObjects(Integer idRelease);

	PReleaseTinySummary findByIdTiny(int id);

	List<PReleaseTrackingToError> listByAllSystemError(String dateRange, int systemId);

	PReleaseReport findByIdReleaseReport(Integer id);

	List<PReleaseReport> listReleaseReport();

	JsonSheet<?> listByAllWithObjects(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId, Integer projectId) throws SQLException, ParseException;

	List<PReleaseReportFast> listReleaseReportFilter(int systemId, int projectId, String dateRange);

	PRelease_RFCFast findRelease_RFCByIdFast(int id);

	PReleaseTrackingShow findReleaseTracking(int id);


	PReleaseSummaryFile findByIdSummaryFile(Integer id);

	JsonSheet<?> listByAllWithOutTracking(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId, Integer projectId) throws SQLException, ParseException;

	PReleaseUserFast findByIdReleaseUserFast(Integer idRelease);

	void updateStatusReleaseUser(PReleaseUserFast release);

	void requestRelease(PReleaseEditWithOutObjects release);

	String getLastStatusHistory(Integer releaseId);

	
	JsonSheet<T> findAllFastRelease(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			Map<String, Object> columns, Criterion qSrch, List<String> fetchs, Map<String, String> alias, Integer veri);
	
}
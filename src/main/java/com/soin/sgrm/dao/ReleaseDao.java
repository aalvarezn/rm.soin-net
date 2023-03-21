package com.soin.sgrm.dao;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.ReleaseEditWithOutObjects;
import com.soin.sgrm.model.ReleaseError;
import com.soin.sgrm.model.ReleaseObjectEdit;
import com.soin.sgrm.model.ReleaseSummary;
import com.soin.sgrm.model.ReleaseSummaryMin;
import com.soin.sgrm.model.ReleaseTinySummary;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.Release_RFC;
import com.soin.sgrm.model.Releases_WithoutObj;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.utils.JsonSheet;
import com.soin.sgrm.utils.ReleaseCreate;

public interface ReleaseDao {
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

	ReleaseSummary findById(Integer id);

	ReleaseEdit findEditById(Integer id) throws SQLException;

	ReleaseUser findReleaseUserById(Integer id) throws SQLException;

	void save(Release release, String tpos) throws Exception;

	void copy(ReleaseEdit release, String tpos) throws Exception;

	List<ReleaseUser> list(String search, String release_id);

	void updateStatusRelease(ReleaseEdit release) throws Exception;

	Release findReleaseById(Integer id);

	void saveRelease(Release release, ReleaseCreate rc) throws Exception;

	void assignRelease(ReleaseEdit release, UserInfo user) throws SQLException;

	void requestRelease(Release release);

	ArrayList<ReleaseObjectEdit> saveReleaseObjects(Integer release_id, ArrayList<ReleaseObjectEdit> objects);

	JsonSheet<?> listReleasesBySystem(int sEcho, int iDisplayStart, int iDisplayLength, String sSearch, Integer systemId) throws SQLException, ParseException;

	Release_RFC findRelease_RFCById(Integer id);

	Integer getDependency(int id);

	void updateStatusReleaseRFC(Release_RFC release,String operator);
  
	Releases_WithoutObj findReleaseWithouObj(Integer id);

	void insertReleaseError(ReleaseError release) throws Exception;

	ReleaseSummaryMin findByIdMin(Integer id);

	ReleaseEditWithOutObjects findEditByIdWithOutObjects(Integer idRelease);

	ReleaseTinySummary findByIdTiny(int id);
	

}
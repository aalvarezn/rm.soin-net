package com.soin.sgrm.dao.wf;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import com.soin.sgrm.model.wf.WFRelease;
import com.soin.sgrm.utils.JsonSheet;

public interface WFReleaseDao {

	List<WFRelease> list();

	WFRelease findWFReleaseById(Integer id);
	
	JsonSheet<?> listWorkFlowRelease(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId) throws SQLException, ParseException;
	
	JsonSheet<?> listWorkFlowManager(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId, Object[] systemsId, Integer userId) throws SQLException, ParseException;
	
	void wfStatusRelease(WFRelease release);
	
	Integer countByType(String group, Object[] ids);

	void wfStatusReleaseWithOutMin(WFRelease release);

}

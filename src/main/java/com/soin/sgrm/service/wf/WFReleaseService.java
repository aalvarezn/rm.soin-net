package com.soin.sgrm.service.wf;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import com.soin.sgrm.model.wf.WFRelease;
import com.soin.sgrm.utils.JsonSheet;

public interface WFReleaseService {

	List<WFRelease> list();
	
	JsonSheet<?> listWorkFlowRelease(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId) throws SQLException, ParseException;
	
	JsonSheet<?> listWorkFlowManager(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Integer statusId, List<Integer> listIdRelease, Integer idUser) throws SQLException, ParseException;
	
	WFRelease findWFReleaseById(Integer id);

	void wfStatusRelease(WFRelease release);
	
	Integer countByType(String group, Object[] ids, Integer idUser);

	void wfStatusReleaseWithOutMin(WFRelease release);
}
package com.soin.sgrm.service.wf;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import com.soin.sgrm.model.wf.WFRFC;
import com.soin.sgrm.utils.JsonSheet;

public interface WFRFCService {

	List<WFRFC> list();
	
	JsonSheet<?> listWorkFlowRFC(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Long statusId) throws SQLException, ParseException;
	
	JsonSheet<?> listWorkFlowManager(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Long statusId, Object[] systemsId) throws SQLException, ParseException;
	
	WFRFC findWFRFCById(Long id);

	void wfStatusRFC(WFRFC release);
	
	
	Integer countByType(String group, Object[] ids);

	void wfStatusRFCWithOutMin(WFRFC release);
}
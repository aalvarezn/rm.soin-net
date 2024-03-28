package com.soin.sgrm.service.pos.wf;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import com.soin.sgrm.model.pos.wf.PWFRFC;
import com.soin.sgrm.utils.JsonSheet;

public interface PWFRFCService {

	List<PWFRFC> list();
	
	JsonSheet<?> listWorkFlowRFC(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Long statusId) throws SQLException, ParseException;
	
	JsonSheet<?> listWorkFlowManager(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Long statusId, Object[] systemsId) throws SQLException, ParseException;
	
	PWFRFC findWFRFCById(Long id);

	void wfStatusRFC(PWFRFC release);
	
	
	Integer countByType(String group, Object[] ids);

	void wfStatusRFCWithOutMin(PWFRFC release);
}
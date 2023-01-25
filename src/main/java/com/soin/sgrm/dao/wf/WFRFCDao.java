package com.soin.sgrm.dao.wf;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import com.soin.sgrm.model.wf.WFRFC;
import com.soin.sgrm.utils.JsonSheet;

public interface WFRFCDao {

	List<WFRFC> list();

	WFRFC findWFRFCById(Long id);
	
	JsonSheet<?> listWorkFlowRFC(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Long statusId) throws SQLException, ParseException;
	
	JsonSheet<?> listWorkFlowManager(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Long statusId, Object[] systemsId) throws SQLException, ParseException;
	
	void wfStatusRFC(WFRFC rfc);
	
	Integer countByType(String group, Object[] ids);

	void wfStatusRFCWithOutMin(WFRFC rfc);

}

package com.soin.sgrm.service.wf;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import com.soin.sgrm.model.wf.WFIncidence;
import com.soin.sgrm.model.wf.WFRelease;
import com.soin.sgrm.utils.JsonSheet;

public interface WFIncidenceService {

	List<WFIncidence> list();
	
	JsonSheet<?> listWorkFlowIncidence(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Long statusId) throws SQLException, ParseException;
	
	JsonSheet<?> listWorkFlowManager(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Long statusId, Object[] systemsId) throws SQLException, ParseException;
	
	WFIncidence findWFIncidenceById(Long idIncidence);

	void wfStatusIncidence(WFIncidence incidence);
	
	Integer countByType(String group, Object[] ids);
}

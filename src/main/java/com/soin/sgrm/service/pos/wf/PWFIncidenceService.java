package com.soin.sgrm.service.pos.wf;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import com.soin.sgrm.model.pos.wf.PWFIncidence;
import com.soin.sgrm.utils.JsonSheet;

public interface PWFIncidenceService {

	List<PWFIncidence> list();
	
	JsonSheet<?> listWorkFlowIncidence(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Long statusId) throws SQLException, ParseException;
	
	JsonSheet<?> listWorkFlowManager(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Long statusId, Object[] systemsId) throws SQLException, ParseException;
	
	PWFIncidence findWFIncidenceById(Long idIncidence);

	void wfStatusIncidence(PWFIncidence incidence);
	
	Integer countByType(String group, Object[] ids);
}

package com.soin.sgrm.dao.wf;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import com.soin.sgrm.model.wf.WFIncidence;
import com.soin.sgrm.utils.JsonSheet;

public interface WFIncidenceDao {

	List<WFIncidence> list();

	WFIncidence findWFIncidenceById(Long id);

	JsonSheet<?> listWorkFlowIncidence(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Long statusId) throws SQLException, ParseException;

	void wfStatusIncidence(WFIncidence incidence);

	JsonSheet<?> listWorkFlowManager(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Long statusId, Object[] systemsId) throws SQLException, ParseException;

	Integer countByType(String group, Object[] ids);



}
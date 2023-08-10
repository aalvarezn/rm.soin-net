package com.soin.sgrm.dao.pos.wf;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import com.soin.sgrm.model.pos.wf.PWFIncidence;
import com.soin.sgrm.utils.JsonSheet;

public interface PWFIncidenceDao {

	List<PWFIncidence> list();

	PWFIncidence findWFIncidenceById(Long id);

	JsonSheet<?> listWorkFlowIncidence(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Long statusId) throws SQLException, ParseException;

	void wfStatusIncidence(PWFIncidence incidence);

	JsonSheet<?> listWorkFlowManager(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			String[] filtred, String[] dateRange, Integer systemId, Long statusId, Object[] systemsId) throws SQLException, ParseException;

	Integer countByType(String group, Object[] ids);



}
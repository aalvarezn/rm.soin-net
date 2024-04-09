package com.soin.sgrm.service.pos;

import java.sql.SQLException;

import com.soin.sgrm.model.pos.PBaseKnowledge;
import com.soin.sgrm.response.JsonSheet;

public interface PBaseKnowledgeService extends BaseService<Long, PBaseKnowledge>{
	public Integer existNumError(String numberError) throws SQLException;
	
	String generateErrorNumber(String component);
	JsonSheet<PBaseKnowledge> findAll1(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch, Long statusId,
			String dateRange, int priorityId,int systemId);

	Integer countByType(Integer id, String type, int query, Object[] ids);

	public JsonSheet<PBaseKnowledge> findAll2(Integer id, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange, Long component);

	Integer countByManager(Integer id, Long idBaseKnowledge);

	public JsonSheet<PBaseKnowledge> findAll2(Integer name, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange, Long componentId, Integer systemId, Integer userLogin,
			boolean rolRM);

}
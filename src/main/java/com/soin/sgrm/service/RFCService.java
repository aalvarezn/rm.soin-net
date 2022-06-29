package com.soin.sgrm.service;

import java.sql.SQLException;

import com.soin.sgrm.model.RFC;
import com.soin.sgrm.response.JsonSheet;

public interface RFCService extends BaseService<Long, RFC>{
	public Integer existNumRelease(String number_release) throws SQLException;
	
	String generateRFCNumber(String codeProject);
	JsonSheet<RFC> findAll1(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch, Long statusId,
			String dateRange, int priorityId,int systemId);

	Integer countByType(Integer id, String type, int query, Object[] ids);

	public JsonSheet<RFC> findAll2(Integer id, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange, int priorityId, int systemId);

	Integer countByManager(Integer id, Long idRFC);

}

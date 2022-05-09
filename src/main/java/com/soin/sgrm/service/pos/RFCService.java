package com.soin.sgrm.service.pos;

import java.sql.SQLException;
import java.text.ParseException;

import com.soin.sgrm.model.pos.PRFC;
import com.soin.sgrm.response.JsonSheet;

public interface RFCService extends BaseService<Long, PRFC> {


	public Integer existNumRelease(String number_release) throws SQLException;
	
	String generateRFCNumber(String codeProject);
	JsonSheet<PRFC> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch, Long statusId,
			String dateRange, Long priorityId,Long impactId);

	Integer countByType(String name, String type, int query, Object[] ids);

}

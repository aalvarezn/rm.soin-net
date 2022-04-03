package com.soin.sgrm.service.pos;

import java.sql.SQLException;
import java.text.ParseException;

import com.soin.sgrm.model.pos.PRFC;
import com.soin.sgrm.response.JsonSheet;

public interface RFCService extends BaseService<Long, PRFC> {

	JsonSheet<PRFC> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Integer sStatus, String dateRange);
	public Integer existNumRelease(String number_release) throws SQLException;
	
	String generateRFCNumber(String codeProject);

}

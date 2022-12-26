package com.soin.sgrm.service;

import com.soin.sgrm.model.RFCError;
import com.soin.sgrm.response.JsonSheet;

public interface RFCErrorService extends BaseService<Long, RFCError>{

	JsonSheet<RFCError> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long errorId, String dateRange, Long sigesId, int systemId);

}
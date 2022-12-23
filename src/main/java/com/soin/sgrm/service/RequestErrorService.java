package com.soin.sgrm.service;

import com.soin.sgrm.model.RequestError;
import com.soin.sgrm.response.JsonSheet;

public interface RequestErrorService extends BaseService<Long, RequestError>{

	JsonSheet<RequestError> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long errorId, String dateRange, Long typePetition, int systemId);

}
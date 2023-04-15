package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.RequestError;
import com.soin.sgrm.response.JsonSheet;

public interface RequestErrorService extends BaseService<Long, RequestError>{

	JsonSheet<RequestError> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long errorId, String dateRange, Long typePetition, int systemId);

	List<RequestError> findAllList(Long errorId, String dateRange, Long typePetitionId, int systemId);

	JsonSheet<RequestError> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long errorId, String dateRange, Long typePetitionId, List<Integer> systemsId);

}
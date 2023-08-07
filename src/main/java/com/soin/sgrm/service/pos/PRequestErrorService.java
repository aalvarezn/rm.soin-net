package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PRequestError;
import com.soin.sgrm.response.JsonSheet;

public interface PRequestErrorService extends BaseService<Long, PRequestError>{

	JsonSheet<PRequestError> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long errorId, String dateRange, Long typePetition, int systemId);

	List<PRequestError> findAllList(Long errorId, String dateRange, Long typePetitionId, int systemId);

	JsonSheet<PRequestError> findAll(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long errorId, String dateRange, Long typePetitionId, List<Integer> systemsId);

}
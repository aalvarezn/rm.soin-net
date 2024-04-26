package com.soin.sgrm.service.pos;

import com.soin.sgrm.model.pos.PRequestBaseR1Fast;
import com.soin.sgrm.response.JsonSheet;

public interface PRequestBaseR1FastService extends BaseService<Long, PRequestBaseR1Fast>{

	JsonSheet<PRequestBaseR1Fast> findAllRequest(Integer name, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange,Integer systemId,Long typePetitionId);


	JsonSheet<PRequestBaseR1Fast> findAllRequest(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long statusId, String dateRange, Integer systemId, Long typePetitionId);


}

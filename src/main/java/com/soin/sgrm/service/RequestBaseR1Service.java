package com.soin.sgrm.service;

import com.soin.sgrm.model.RequestBaseR1;
import com.soin.sgrm.response.JsonSheet;

public interface RequestBaseR1Service extends BaseService<Long, RequestBaseR1>{

	JsonSheet<RequestBaseR1> findAllRequest(Integer name, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange,Integer systemId,Long typePetitionId);


	JsonSheet<RequestBaseR1> findAllRequest(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long statusId, String dateRange, Integer systemId, Long typePetitionId);


	Integer countByType(Integer id, String type, int query, Object[] ids);

	RequestBaseR1 findByR1(Long id);

}

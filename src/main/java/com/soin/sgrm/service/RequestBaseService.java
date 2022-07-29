package com.soin.sgrm.service;

import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.response.JsonSheet;

public interface RequestBaseService extends BaseService<Long, RequestBase>{

	JsonSheet<RequestBase> findAllRequest(Integer name, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange,Integer systemId,Long typePetitionId);

	String generateRequestNumber(String codeProyect,String description);

	JsonSheet<RequestBase> findAllRequest(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long statusId, String dateRange, Integer systemId, Long typePetitionId);


	Integer countByType(Integer id, String type, int query, Object[] ids);

}

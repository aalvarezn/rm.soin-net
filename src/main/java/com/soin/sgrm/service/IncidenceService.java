package com.soin.sgrm.service;

import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.RequestBaseR1;
import com.soin.sgrm.response.JsonSheet;

public interface IncidenceService extends BaseService<Long, Incidence>{

	JsonSheet<Incidence> findAllRequest(Integer name, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange,Integer systemId,Long typePetitionId);

	String generateRequestNumber(String codeProyect,String description);

	JsonSheet<Incidence> findAllRequest(Integer sEcho, Integer iDisplayStart, Integer iDisplayLength, String sSearch,
			Long statusId, String dateRange, Integer systemId, Long typePetitionId);


	Integer countByType(Integer id, String type, int query, Object[] ids);

	RequestBaseR1 findByR1(Long id);

}

package com.soin.sgrm.service;

import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.RequestBaseR1;
import com.soin.sgrm.response.JsonSheet;

public interface IncidenceService extends BaseService<Long, Incidence>{

	

	String generatTicketNumber(String nameSystem);


	Integer countByType(Integer id, String type, int query, Object[] ids);

	

	JsonSheet<Incidence> findAllRequest( Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange, Long typeId, Long priorityId);


	Incidence getIncidences(Long id);

	Incidence getIncidenceByName(String numTicket);
	

	JsonSheet<Incidence> findAllRequest(String email, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange, Long typeId, Long priorityId);

}

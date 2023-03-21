package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.IncidenceResume;
import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.RequestBaseR1;
import com.soin.sgrm.response.JsonSheet;

public interface IncidenceService extends BaseService<Long, Incidence>{

	

	String generatTicketNumber(String nameSystem, String typeCode);



	Integer countByType(Integer id, String type, int query, Object[] ids,Integer userLogin,String email);


	JsonSheet<Incidence> findAllRequest( Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange, Long typeId, Long priorityId);


	Incidence getIncidences(Long id);

	Incidence getIncidenceByName(String numTicket);
	
	List<IncidenceResume> getListIncideSLA();
	
	void updateIncidenceResume(IncidenceResume incidenceResume);
	

	JsonSheet<Incidence> findAllRequest(String email, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange, Long typeId, Long priorityId, Integer userLogin,
			Integer systemId);
	JsonSheet<Incidence> findAllRequest2(String email, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange, Long typeId, Long priorityId, Integer systemId ,
			Integer userLogin );


	List<IncidenceResume> getListIncideRequest();


}

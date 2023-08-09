package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PIncidence;
import com.soin.sgrm.model.pos.PIncidenceResume;
import com.soin.sgrm.response.JsonSheet;

public interface PIncidenceService extends BaseService<Long, PIncidence>{

	

	String generatTicketNumber(String nameSystem, String typeCode);



	Integer countByType(Integer id, String type, int query, Object[] ids,Integer userLogin,String email);


	JsonSheet<PIncidence> findAllRequest( Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange, Long typeId, Long priorityId);


	PIncidence getIncidences(Long id);

	PIncidence getIncidenceByName(String numTicket);
	
	List<PIncidenceResume> getListIncideSLA();
	
	void updateIncidenceResume(PIncidenceResume incidenceResume);
	

	JsonSheet<PIncidence> findAllRequest(String email, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange, Long typeId, Long priorityId, Integer userLogin,
			Integer systemId);
	JsonSheet<PIncidence> findAllRequest2(String email, Integer sEcho, Integer iDisplayStart, Integer iDisplayLength,
			String sSearch, Long statusId, String dateRange, Long typeId, Long priorityId, Integer systemId ,
			Integer userLogin );


	List<PIncidenceResume> getListIncideRequest();


}
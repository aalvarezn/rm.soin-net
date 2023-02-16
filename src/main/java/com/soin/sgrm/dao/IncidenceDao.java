package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.Incidence;
import com.soin.sgrm.model.IncidenceResume;
import com.soin.sgrm.model.RequestBase;
import com.soin.sgrm.model.RequestBaseR1;

public interface IncidenceDao extends BaseDao<Long, Incidence> {

	Integer existNumTicket(String numRequest);

	Integer countByManager(Integer id, Long idRequest);

	Integer countByType(Integer id, String type, int query, Object[] ids,Integer userLogin,String email);

	Incidence getIncidences(Long id);

	Incidence getIncidenceByName(String numTicket);

	List<IncidenceResume> getListIncideSLA();

	void updateIncidenceResume(IncidenceResume incidenceResume);

	List<IncidenceResume> getListIncideRequest();

	Integer countByTypeBySystem(Integer id, String type, int query, Object[] ids, Integer userLogin, String email); 

}

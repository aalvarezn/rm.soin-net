package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PIncidence;
import com.soin.sgrm.model.pos.PIncidenceResume;




public interface PIncidenceDao extends BaseDao<Long, PIncidence> {

	Integer existNumTicket(String numRequest);

	Integer countByManager(Integer id, Long idRequest);

	Integer countByType(Integer id, String type, int query, Object[] ids,Integer userLogin,String email);

	PIncidence getIncidences(Long id);

	PIncidence getIncidenceByName(String numTicket);

	List<PIncidenceResume> getListIncideSLA();

	void updateIncidenceResume(PIncidenceResume incidenceResume);

	List<PIncidenceResume> getListIncideRequest();

	Integer countByTypeBySystem(Integer id, String type, int query, Object[] ids, Integer userLogin, String email); 

}
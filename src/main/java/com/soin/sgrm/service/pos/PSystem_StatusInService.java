package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PSystem_StatusIn;


public interface PSystem_StatusInService extends BaseService<Long,PSystem_StatusIn> {

	List<PSystem_StatusIn> listTypePetition();

	List<PSystem_StatusIn> findBySystem(Integer id);

	PSystem_StatusIn findByIdAndSys(Integer systemId, Long statusId);
	
	PSystem_StatusIn findByIdByCode(int id, String code);

	List<PSystem_StatusIn> findByManger(Integer idUser);

	PSystem_StatusIn findByIdByName(int id, String string);



}

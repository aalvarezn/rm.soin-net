package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.System_Priority;
import com.soin.sgrm.model.System_StatusIn;

public interface System_StatusInService extends BaseService<Long,System_StatusIn> {

	List<System_StatusIn> listTypePetition();

	List<System_StatusIn> findBySystem(Integer id);

	System_StatusIn findByIdAndSys(Integer systemId, Long statusId);
	
	System_StatusIn findByIdByCode(int id, String code);

	List<System_StatusIn> findByManger(Integer idUser);

	System_StatusIn findByIdByName(int id, String string);



}

package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.System_StatusIn;

public interface System_StatusInDao extends BaseDao<Long, System_StatusIn> {

	List<System_StatusIn> listTypePetition();

	List<System_StatusIn> findBySystem(Integer id);

	System_StatusIn findByIdAndSys(Integer systemId, Long priorityId);

	List<System_StatusIn> findByManger(Integer idUser);

	System_StatusIn findByIdByCode(int id, String code);

	System_StatusIn findByIdByName(int id, String name);

}

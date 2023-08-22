package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PSystem_StatusIn;



public interface PSystem_StatusInDao extends BaseDao<Long, PSystem_StatusIn> {

	List<PSystem_StatusIn> listTypePetition();

	List<PSystem_StatusIn> findBySystem(Integer id);

	PSystem_StatusIn findByIdAndSys(Integer systemId, Long priorityId);

	List<PSystem_StatusIn> findByManger(Integer idUser);

	PSystem_StatusIn findByIdByCode(int id, String code);

	PSystem_StatusIn findByIdByName(int id, String name);

}

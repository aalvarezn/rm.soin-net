package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PSystem_Priority;

public interface PSystem_PriorityDao extends BaseDao<Long, PSystem_Priority> {

	List<PSystem_Priority> listTypePetition();

	List<PSystem_Priority> findBySystem(Integer id);

	PSystem_Priority findByIdAndSys(Integer systemId, Long priorityId);

	List<PSystem_Priority> findByManger(Integer idUser);

}
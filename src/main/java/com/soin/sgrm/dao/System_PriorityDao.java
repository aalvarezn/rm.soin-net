package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.System_Priority;
import com.soin.sgrm.model.TypeIncidence;

public interface System_PriorityDao extends BaseDao<Long, System_Priority> {

	List<System_Priority> listTypePetition();

	List<System_Priority> findBySystem(Integer id);

	System_Priority findByIdAndSys(Integer systemId, Long priorityId);

	List<System_Priority> findByManger(Integer idUser);

}

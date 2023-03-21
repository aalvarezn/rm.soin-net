package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.System_Priority;

public interface System_PriorityService extends BaseService<Long,System_Priority> {

	List<System_Priority> listTypePetition();

	List<System_Priority> findBySystem(Integer id);

	System_Priority findByIdAndSys(Integer systemId, Long priorityId);

	List<System_Priority> findByManger(Integer idUser);

}
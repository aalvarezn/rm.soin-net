package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.SystemTypeIncidence;

public interface SystemTypeIncidenceService extends BaseService<Long,SystemTypeIncidence> {

	List<SystemTypeIncidence> listTypePetition();

	List<SystemTypeIncidence> findBySystem(Integer id);

	SystemTypeIncidence findByIdAndSys(Integer systemId, Long typeIncidence);

	List<SystemTypeIncidence> findByManager(Integer idUser);

}

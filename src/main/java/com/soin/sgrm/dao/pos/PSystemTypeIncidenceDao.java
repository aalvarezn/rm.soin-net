package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.SystemTypeIncidence;

public interface PSystemTypeIncidenceDao extends BaseDao<Long, SystemTypeIncidence> {

	List<SystemTypeIncidence> listTypePetition();

	List<SystemTypeIncidence> findBySystem(Integer id);

	SystemTypeIncidence findByIdAndSys(Integer systemId, Long typeIncidenceId);

	List<SystemTypeIncidence> findByManager(Integer idUser);

}
package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PSystemTypeIncidence;



public interface PSystemTypeIncidenceDao extends BaseDao<Long, PSystemTypeIncidence> {

	List<PSystemTypeIncidence> listTypePetition();

	List<PSystemTypeIncidence> findBySystem(Integer id);

	PSystemTypeIncidence findByIdAndSys(Integer systemId, Long typeIncidenceId);

	List<PSystemTypeIncidence> findByManager(Integer idUser);

}
package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PEmailIncidence;

public interface PEmailIncidenceDao extends BaseDao<Long, PEmailIncidence> {

	List<PEmailIncidence> listTypePetition();

	List<PEmailIncidence> findBySystem(Integer id);

	PEmailIncidence findByIdAndSys(Integer systemId, Long typeIncidenceId);

}

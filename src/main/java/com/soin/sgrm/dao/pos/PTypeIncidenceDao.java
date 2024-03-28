package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PTypeIncidence;

public interface PTypeIncidenceDao extends BaseDao<Long, PTypeIncidence> {

	List<PTypeIncidence> listTypePetition();

}

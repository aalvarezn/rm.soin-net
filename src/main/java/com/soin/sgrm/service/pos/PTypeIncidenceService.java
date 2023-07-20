package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PTypeIncidence;

public interface PTypeIncidenceService extends BaseService<Long,PTypeIncidence> {

	List<PTypeIncidence> listTypePetition();

}

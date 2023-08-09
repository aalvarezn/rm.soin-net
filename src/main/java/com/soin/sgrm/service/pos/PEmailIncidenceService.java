package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.EmailIncidence;
import com.soin.sgrm.model.pos.PEmailIncidence;

public interface PEmailIncidenceService extends BaseService<Long,PEmailIncidence> {

	List<PEmailIncidence> listTypePetition();

	List<PEmailIncidence> findBySystem(Integer id);

	PEmailIncidence findByIdAndSys(Integer systemId, Long typeIncidence);

}

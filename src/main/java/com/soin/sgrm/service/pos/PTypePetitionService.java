package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.model.pos.PTypePetition;

public interface PTypePetitionService extends BaseService<Long,PTypePetition> {

	List<PTypePetition> listTypePetition();


}

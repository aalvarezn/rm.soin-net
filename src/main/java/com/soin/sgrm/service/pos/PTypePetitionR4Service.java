package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.model.TypePetitionR4;
import com.soin.sgrm.model.pos.PTypePetitionR4;

public interface PTypePetitionR4Service extends BaseService<Long,PTypePetitionR4> {

	List<PTypePetitionR4> listTypePetition();

}

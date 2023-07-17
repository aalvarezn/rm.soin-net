package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.model.pos.PTypePetition;

public interface PTypePetitionDao extends BaseDao<Long, PTypePetition> {

	List<PTypePetition> listTypePetition();

}

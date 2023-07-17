package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PTypePetitionR4;

public interface PTypePetitionR4Dao extends BaseDao<Long, PTypePetitionR4> {

	List<PTypePetitionR4> listTypePetition();

}

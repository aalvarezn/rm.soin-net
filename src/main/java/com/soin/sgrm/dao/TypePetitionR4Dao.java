package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.model.TypePetitionR4;

public interface TypePetitionR4Dao extends BaseDao<Long, TypePetitionR4> {

	List<TypePetitionR4> listTypePetition();

}

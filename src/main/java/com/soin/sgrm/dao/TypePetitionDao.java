package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.TypePetition;

public interface TypePetitionDao extends BaseDao<Long, TypePetition> {

	List<TypePetition> listTypePetition();

}

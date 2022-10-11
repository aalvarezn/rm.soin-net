package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.TypePetition;

public interface TypePetitionService extends BaseService<Long,TypePetition> {

	List<TypePetition> listTypePetition();

}

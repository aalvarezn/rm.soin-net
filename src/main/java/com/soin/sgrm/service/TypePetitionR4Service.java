package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.model.TypePetitionR4;

public interface TypePetitionR4Service extends BaseService<Long,TypePetitionR4> {

	List<TypePetitionR4> listTypePetition();

}

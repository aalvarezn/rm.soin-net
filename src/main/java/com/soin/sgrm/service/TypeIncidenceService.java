package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.TypeIncidence;

public interface TypeIncidenceService extends BaseService<Long,TypeIncidence> {

	List<TypeIncidence> listTypePetition();

}

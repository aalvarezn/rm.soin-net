package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.TypeIncidence;

public interface TypeIncidenceDao extends BaseDao<Long, TypeIncidence> {

	List<TypeIncidence> listTypePetition();

}

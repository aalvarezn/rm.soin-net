package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PParameter;

public interface PParameterDao {

	List<PParameter> listAll();

	PParameter findByCode(Integer code);

	PParameter findById(Integer id);

	void updatePParameter(PParameter param);

	PParameter getPParameterByCode(Integer code);

}

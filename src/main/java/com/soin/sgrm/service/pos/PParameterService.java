package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PParameter;

public interface PParameterService {

	List<PParameter> listAll();

	PParameter findByCode(Integer code);

	PParameter findById(Integer id);

	void updateParameter(PParameter param);

	PParameter getParameterByCode(Integer code);
}

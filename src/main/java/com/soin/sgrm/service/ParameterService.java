package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.Parameter;

public interface ParameterService {

	List<Parameter> listAll();

	Parameter findByCode(Integer code);

	Parameter findById(Integer id);

	void updateParameter(Parameter param);
}

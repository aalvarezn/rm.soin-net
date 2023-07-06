package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.Parameter;

public interface ParameterDao {

	List<Parameter> listAll();

	Parameter findByCode(Integer code);

	Parameter findById(Integer id);

	void updateParameter(Parameter param);

	Parameter getParameterByCode(Integer code);

}

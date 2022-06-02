package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ParameterDao;
import com.soin.sgrm.model.Parameter;

@Transactional("transactionManager")
@Service("ParameterService")
public class ParameterServiceImpl implements ParameterService {

	@Autowired
	ParameterDao dao;

	@Override
	public List<Parameter> listAll() {
		return dao.listAll();
	}

	@Override
	public Parameter findByCode(Integer code) {
		return dao.findByCode(code);
	}

	@Override
	public Parameter findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void updateParameter(Parameter param) {
		dao.updateParameter(param);
	}

	@Override
	public Parameter getParameterByCode(Integer code) {
		return dao.getParameterByCode(code);
	}

}

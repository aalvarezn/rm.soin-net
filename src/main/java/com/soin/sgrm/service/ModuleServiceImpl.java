package com.soin.sgrm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ModuleDao;
import com.soin.sgrm.model.Module;

@Transactional("transactionManager")
@Service("ModuleService")
public class ModuleServiceImpl implements ModuleService {

	@Autowired
	ModuleDao dao;

	@Override
	public Module findBySystemId(String code) {
		return dao.findBySystemId(code);
	}

	@Override
	public Module findById(Integer id) {
		return dao.findById(id);
	}

}

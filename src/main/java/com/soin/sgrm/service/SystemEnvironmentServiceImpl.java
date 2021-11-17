package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.SystemEnvironmentDao;
import com.soin.sgrm.model.SystemEnvironment;

@Transactional("transactionManager")
@Service("SystemEnvironmentService")
public class SystemEnvironmentServiceImpl implements SystemEnvironmentService {

	@Autowired
	SystemEnvironmentDao environmentDao;

	@Override
	public List<SystemEnvironment> listBySystem(Integer id) {
		return environmentDao.listBySystem(id);
	}

}

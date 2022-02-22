package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.SystemConfigurationDao;
import com.soin.sgrm.model.SystemConfiguration;

@Transactional("transactionManager")
@Service("SystemConfigurationService")
public class SystemConfigurationServiceImpl implements SystemConfigurationService {

	@Autowired
	SystemConfigurationDao dao;

	@Override
	public SystemConfiguration findBySystemId(Integer id) {
		return dao.findBySystemId(id);
	}

	@Override
	public List<SystemConfiguration> list() {
		return dao.list();
	}

	@Override
	public SystemConfiguration findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public SystemConfiguration update(SystemConfiguration systemConfig) {
		return dao.update(systemConfig);
	}

	@Override
	public void save(SystemConfiguration systemConfig) {
		dao.save(systemConfig);
	}

}

package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.SystemConfiguration;

public interface SystemConfigurationDao {

	SystemConfiguration findBySystemId(Integer id);

	SystemConfiguration findById(Integer id);

	List<SystemConfiguration> list();

	SystemConfiguration update(SystemConfiguration systemConfig);
	
	void save(SystemConfiguration systemConfig);
}

package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.SystemConfiguration;

public interface SystemConfigurationService {

	SystemConfiguration findBySystemId(Integer id);

	SystemConfiguration findById(Integer id);

	List<SystemConfiguration> list();

	SystemConfiguration update(SystemConfiguration systemConfig);
	
	void save(SystemConfiguration systemConfig);

}

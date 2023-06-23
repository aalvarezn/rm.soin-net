package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.SystemConfiguration;
import com.soin.sgrm.model.pos.PSystemConfiguration;

public interface PSystemConfigurationService {

	PSystemConfiguration findBySystemId(Integer id);

	PSystemConfiguration findById(Integer id);

	List<PSystemConfiguration> list();

	PSystemConfiguration update(PSystemConfiguration systemConfig);
	
	void save(PSystemConfiguration systemConfig);

}

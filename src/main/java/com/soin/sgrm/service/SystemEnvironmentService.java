package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.SystemEnvironment;

public interface SystemEnvironmentService {
	
	List<SystemEnvironment> listBySystem(Integer id);

}

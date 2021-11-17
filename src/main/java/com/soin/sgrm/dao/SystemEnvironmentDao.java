package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.SystemEnvironment;

public interface SystemEnvironmentDao {
	
	List<SystemEnvironment> listBySystem(Integer id);

}

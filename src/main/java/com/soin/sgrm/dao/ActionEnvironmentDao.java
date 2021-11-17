package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.ActionEnvironment;
import com.soin.sgrm.model.ReleaseActionEdit;

public interface ActionEnvironmentDao {
	
	List<ActionEnvironment> listBySystem(Integer id);
	
	ReleaseActionEdit findById(Integer id);
	
	ReleaseActionEdit addReleaseAction(ReleaseActionEdit action, Integer release_id) throws Exception;
	
	void deleteReleaseDependency(Integer action_id, Integer release_id) throws Exception;
	

}

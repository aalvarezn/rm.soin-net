package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.ActionEnvironment;
import com.soin.sgrm.model.ReleaseActionEdit;
import com.soin.sgrm.model.pos.PActionEnvironment;
import com.soin.sgrm.model.pos.PReleaseActionEdit;

public interface PActionEnvironmentDao {
	
	List<PActionEnvironment> listBySystem(Integer id);
	
	PReleaseActionEdit findById(Integer id);
	
	PReleaseActionEdit addReleaseAction(PReleaseActionEdit action, Integer release_id) throws Exception;
	
	void deleteReleaseDependency(Integer action_id, Integer release_id) throws Exception;
	
	List<PActionEnvironment> list();
	
	PActionEnvironment findActionById(Integer id);

	void save(PActionEnvironment action);

	void update(PActionEnvironment action);

	void delete(Integer id);
	

}

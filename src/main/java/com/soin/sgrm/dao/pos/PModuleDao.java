package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.Module;
import com.soin.sgrm.model.pos.PModule;

public interface PModuleDao {

	PModule findBySystemId(String code);

	List<PModule> list();
	
	PModule findById(Integer id);

	void save(PModule module);

	void update(PModule module);

	void delete(Integer id);

}

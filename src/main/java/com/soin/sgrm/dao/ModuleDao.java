package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.Module;

public interface ModuleDao {

	Module findBySystemId(String code);

	List<Module> list();
	
	Module findById(Integer id);

	void save(Module module);

	void update(Module module);

	void delete(Integer id);

}

package com.soin.sgrm.dao;

import com.soin.sgrm.model.Module;

public interface ModuleDao {

	Module findBySystemId(String code);

	Module findById(Integer id);

}

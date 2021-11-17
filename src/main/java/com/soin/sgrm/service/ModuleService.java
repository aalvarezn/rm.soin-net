package com.soin.sgrm.service;

import com.soin.sgrm.model.Module;

public interface ModuleService {

	Module findBySystemId(String code);

	Module findById(Integer id);

}

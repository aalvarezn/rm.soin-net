package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.ConfigurationItem;

public interface ConfigurationItemService {

	List<ConfigurationItem> listBySystem(Integer id);
	
	ConfigurationItem save (ConfigurationItem confItem);

	ConfigurationItem findById(Integer id);

	ConfigurationItem findByName(String name, Integer system_id);

	boolean existItem(String name, Integer system_id);

}

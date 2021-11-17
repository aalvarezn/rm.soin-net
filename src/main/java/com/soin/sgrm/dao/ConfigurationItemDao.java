package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.ConfigurationItem;

public interface ConfigurationItemDao {

	List<ConfigurationItem> listBySystem(Integer id);
	
	ConfigurationItem save(ConfigurationItem confItem);

	ConfigurationItem findById(Integer id);
	
	ConfigurationItem findByName(String name, Integer system_id);

	boolean existItem(String name, Integer system_id);

}

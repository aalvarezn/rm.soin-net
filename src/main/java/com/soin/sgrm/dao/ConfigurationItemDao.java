package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.ConfigurationItem;

public interface ConfigurationItemDao {

	List<ConfigurationItem> listBySystem(Integer id);

	ConfigurationItem findByName(String name, Integer system_id);

	boolean existItem(String name, Integer system_id);

	List<ConfigurationItem> list();

	ConfigurationItem findById(Integer id);

	void save(ConfigurationItem configurationItem);

	void update(ConfigurationItem configurationItem);

	void delete(Integer id);

}

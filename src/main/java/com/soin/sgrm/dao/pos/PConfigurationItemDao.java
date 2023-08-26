package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.ConfigurationItem;
import com.soin.sgrm.model.pos.PConfigurationItem;

public interface PConfigurationItemDao {

	List<PConfigurationItem> listBySystem(Integer id);

	PConfigurationItem findByName(String name, Integer system_id);

	boolean existItem(String name, Integer system_id);

	List<PConfigurationItem> list();

	PConfigurationItem findById(Integer id);

	void save(PConfigurationItem configurationItem);

	void update(PConfigurationItem configurationItem);

	void delete(Integer id);

}
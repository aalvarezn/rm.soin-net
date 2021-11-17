package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ConfigurationItemDao;
import com.soin.sgrm.model.ConfigurationItem;

@Transactional("transactionManager")
@Service("ConfigurationItemService")
public class ConfigurationItemServiceImpl implements ConfigurationItemService {

	@Autowired
	ConfigurationItemDao dao;

	@Override
	public List<ConfigurationItem> listBySystem(Integer id) {
		return dao.listBySystem(id);
	}

	@Override
	public ConfigurationItem findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public boolean existItem(String name, Integer system_id) {
		return dao.existItem(name, system_id);
	}

	@Override
	public ConfigurationItem findByName(String name, Integer system_id) {
		return dao.findByName(name, system_id);
	}

	@Override
	public ConfigurationItem save(ConfigurationItem confItem) {
		return dao.save(confItem);
	}

}

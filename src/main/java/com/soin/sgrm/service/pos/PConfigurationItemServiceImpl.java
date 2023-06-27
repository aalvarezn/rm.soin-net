package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ConfigurationItemDao;
import com.soin.sgrm.dao.pos.PConfigurationItemDao;
import com.soin.sgrm.model.ConfigurationItem;
import com.soin.sgrm.model.pos.PConfigurationItem;

@Transactional("transactionManagerPos")
@Service("PConfigurationItemService")
public class PConfigurationItemServiceImpl implements PConfigurationItemService {

	@Autowired
	PConfigurationItemDao dao;

	@Override
	public List<PConfigurationItem> listBySystem(Integer id) {
		return dao.listBySystem(id);
	}

	@Override
	public PConfigurationItem findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public boolean existItem(String name, Integer system_id) {
		return dao.existItem(name, system_id);
	}

	@Override
	public PConfigurationItem findByName(String name, Integer system_id) {
		return dao.findByName(name, system_id);
	}

	@Override
	public void save(PConfigurationItem confItem) {
		dao.save(confItem);
	}

	@Override
	public List<PConfigurationItem> list() {
		return dao.list();
	}

	@Override
	public void update(PConfigurationItem configurationItem) {
		dao.update(configurationItem);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

}

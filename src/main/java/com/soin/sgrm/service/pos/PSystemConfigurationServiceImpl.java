package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.SystemConfigurationDao;
import com.soin.sgrm.dao.pos.PSystemConfigurationDao;
import com.soin.sgrm.model.SystemConfiguration;
import com.soin.sgrm.model.pos.PSystemConfiguration;

@Transactional("transactionManagerPos")
@Service("PSystemConfigurationService")
public class PSystemConfigurationServiceImpl implements PSystemConfigurationService {

	@Autowired
	PSystemConfigurationDao dao;

	@Override
	public PSystemConfiguration findBySystemId(Integer id) {
		return dao.findBySystemId(id);
	}

	@Override
	public List<PSystemConfiguration> list() {
		return dao.list();
	}

	@Override
	public PSystemConfiguration findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public PSystemConfiguration update(PSystemConfiguration systemConfig) {
		return dao.update(systemConfig);
	}

	@Override
	public void save(PSystemConfiguration systemConfig) {
		dao.save(systemConfig);
	}

}

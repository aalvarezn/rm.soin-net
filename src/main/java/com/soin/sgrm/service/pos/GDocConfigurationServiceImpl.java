package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.GDocConfigurationDao;
import com.soin.sgrm.model.pos.PGDocConfiguration;

@Service("gDocConfigurationService")
@Transactional("transactionManagerPos")
public class GDocConfigurationServiceImpl implements GDocConfigurationService {
	@Autowired
	GDocConfigurationDao dao;
	
	@Override
	public PGDocConfiguration findById(Long id) {

		return dao.getById(id);
	}

	@Override
	public PGDocConfiguration findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<PGDocConfiguration> findAll() {

		return dao.findAll();
	}

	@Override
	public void save(PGDocConfiguration model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PGDocConfiguration model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PGDocConfiguration model) {
		dao.update(model);
	}



}

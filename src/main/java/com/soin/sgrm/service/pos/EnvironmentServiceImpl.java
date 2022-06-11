package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.EnvironmentDao;
import com.soin.sgrm.model.pos.PEnvironment;

@Service("environmentService")
@Transactional("transactionManagerPos")
public class EnvironmentServiceImpl implements EnvironmentService {
	@Autowired
	EnvironmentDao dao;

	@Override
	public PEnvironment findById(Long id) {

		return dao.getById(id);
	}

	@Override
	public PEnvironment findByKey(String name, String value) {

		return dao.getByKey(name, value);
	}

	@Override
	public List<PEnvironment> findAll() {

		return dao.findAll();
	}

	@Override
	public void save(PEnvironment model) {
		dao.save(model);

	}

	@Override
	public void delete(Long id) {
		PEnvironment model = findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PEnvironment model) {
		dao.update(model);
	}

}

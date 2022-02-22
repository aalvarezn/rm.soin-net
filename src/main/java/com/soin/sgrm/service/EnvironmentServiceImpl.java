package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.EnvironmentDao;
import com.soin.sgrm.model.Environment;

@Transactional("transactionManager")
@Service("EnvironmentService")
public class EnvironmentServiceImpl implements EnvironmentService {

	@Autowired
	EnvironmentDao dao;

	@Override
	public List<Environment> listBySystem(Integer id) {
		return dao.listBySystem(id);
	}

	@Override
	public List<Environment> list() {
		return dao.list();
	}

	@Override
	public Environment findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void save(Environment environment) {
		dao.save(environment);
	}

	@Override
	public void update(Environment environment) {
		dao.update(environment);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}
}

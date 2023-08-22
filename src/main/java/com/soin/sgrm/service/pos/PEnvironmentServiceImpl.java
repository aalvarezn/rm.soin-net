package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.EnvironmentDao;
import com.soin.sgrm.dao.pos.PEnvironmentDao;
import com.soin.sgrm.model.Environment;
import com.soin.sgrm.model.pos.PEnvironment;

@Transactional("transactionManagerPos")
@Service("PEnvironmentService")
public class PEnvironmentServiceImpl implements PEnvironmentService {

	@Autowired
	PEnvironmentDao dao;

	@Override
	public List<PEnvironment> listBySystem(Integer id) {
		return dao.listBySystem(id);
	}

	@Override
	public List<PEnvironment> list() {
		return dao.list();
	}

	@Override
	public PEnvironment findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void save(PEnvironment environment) {
		dao.save(environment);
	}

	@Override
	public void update(PEnvironment environment) {
		dao.update(environment);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}
}

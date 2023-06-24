package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ActionEnvironmentDao;
import com.soin.sgrm.dao.pos.PActionEnvironmentDao;
import com.soin.sgrm.model.ActionEnvironment;
import com.soin.sgrm.model.ReleaseActionEdit;
import com.soin.sgrm.model.pos.PActionEnvironment;
import com.soin.sgrm.model.pos.PReleaseActionEdit;

@Transactional("transactionManagerPos")
@Service("PActionEnvironmentService")
public class PActionEnvironmentServiceImpl implements PActionEnvironmentService {

	@Autowired
	PActionEnvironmentDao dao;

	@Override
	public List<PActionEnvironment> listBySystem(Integer id) {
		return dao.listBySystem(id);
	}

	@Override
	public PReleaseActionEdit addReleaseAction(PReleaseActionEdit action, Integer release_id) throws Exception {
		return dao.addReleaseAction(action, release_id);
	}

	@Override
	public void deleteReleaseDependency(Integer action_id, Integer release_id) throws Exception {
		dao.deleteReleaseDependency(action_id, release_id);
	}

	@Override
	public List<PActionEnvironment> list() {
		return dao.list();
	}

	@Override
	public PActionEnvironment findActionById(Integer id) {
		return dao.findActionById(id);
	}

	@Override
	public void save(PActionEnvironment action) {
		dao.save(action);
	}

	@Override
	public void update(PActionEnvironment action) {
		dao.update(action);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

}

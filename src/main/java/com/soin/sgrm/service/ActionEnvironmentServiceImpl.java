package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ActionEnvironmentDao;
import com.soin.sgrm.model.ActionEnvironment;
import com.soin.sgrm.model.ReleaseActionEdit;

@Transactional("transactionManager")
@Service("ActionEnvironmentService")
public class ActionEnvironmentServiceImpl implements ActionEnvironmentService {

	@Autowired
	ActionEnvironmentDao dao;

	@Override
	public List<ActionEnvironment> listBySystem(Integer id) {
		return dao.listBySystem(id);
	}

	@Override
	public ReleaseActionEdit addReleaseAction(ReleaseActionEdit action, Integer release_id) throws Exception {
		return dao.addReleaseAction(action, release_id);
	}

	@Override
	public void deleteReleaseDependency(Integer action_id, Integer release_id) throws Exception {
		dao.deleteReleaseDependency(action_id, release_id);
	}

	@Override
	public List<ActionEnvironment> list() {
		return dao.list();
	}

	@Override
	public ActionEnvironment findActionById(Integer id) {
		return dao.findActionById(id);
	}

	@Override
	public void save(ActionEnvironment action) {
		dao.save(action);
	}

	@Override
	public void update(ActionEnvironment action) {
		dao.update(action);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

}

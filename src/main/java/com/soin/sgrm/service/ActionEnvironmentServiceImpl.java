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

}

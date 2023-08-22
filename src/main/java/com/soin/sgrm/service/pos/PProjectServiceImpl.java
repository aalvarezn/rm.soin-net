package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ProjectDao;
import com.soin.sgrm.dao.pos.PProjectDao;
import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.pos.PProject;

@Transactional("transactionManagerPos")
@Service("PProjectService")
public class PProjectServiceImpl implements PProjectService {

	@Autowired
	PProjectDao dao;

	@Override
	public List<PProject> listAll() {
		return dao.listAll();
	}

	@Override
	public PProject findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void save(PProject project) {
		dao.save(project);
	}

	@Override
	public void update(PProject project) {
		dao.update(project);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}
}

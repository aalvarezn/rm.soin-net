package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.ProjectDao;
import com.soin.sgrm.model.pos.PProject;

@Service("projectService")
@Transactional("transactionManagerPos")
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	ProjectDao dao;
	
	@Override
	public PProject findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PProject findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public List<PProject> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PProject model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PProject model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PProject model) {
		dao.update(model);
	}

}

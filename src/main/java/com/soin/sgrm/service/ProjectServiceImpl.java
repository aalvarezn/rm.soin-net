package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ProjectDao;
import com.soin.sgrm.model.Project;

@Transactional("transactionManager")
@Service("ProjectService")
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	ProjectDao dao;

	@Override
	public List<Project> listAll() {
		return dao.listAll();
	}

	@Override
	public Project findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void save(Project project) {
		dao.save(project);
	}

	@Override
	public void update(Project project) {
		dao.update(project);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}
}

package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.Project;

public interface ProjectService {

	List<Project> listAll();

	Project findById(Integer id);

	void save(Project project);

	void update(Project project);

	void delete(Integer id);

}

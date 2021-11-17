package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.Project;

public interface ProjectDao {

	List<Project> listAll();

	Project findById(Integer id);

	void save(Project project);

	void update(Project project);

	void delete(Integer id);

}

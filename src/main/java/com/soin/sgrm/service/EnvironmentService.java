package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.Environment;

public interface EnvironmentService {

	List<Environment> listBySystem(Integer id);

	List<Environment> list();

	Environment findById(Integer id);

	void save(Environment environment);

	void update(Environment environment);

	void delete(Integer id);

}

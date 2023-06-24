package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.Environment;
import com.soin.sgrm.model.pos.PEnvironment;

public interface PEnvironmentService {

	List<PEnvironment> listBySystem(Integer id);

	List<PEnvironment> list();

	PEnvironment findById(Integer id);

	void save(PEnvironment environment);

	void update(PEnvironment environment);

	void delete(Integer id);

}

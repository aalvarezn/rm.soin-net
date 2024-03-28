package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.Project;
import com.soin.sgrm.model.pos.PProject;

public interface PProjectService {

	List<PProject> listAll();

	PProject findById(Integer id);

	void save(PProject project);

	void update(PProject project);

	void delete(Integer id);

}

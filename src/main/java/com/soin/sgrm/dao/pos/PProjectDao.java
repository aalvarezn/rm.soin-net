package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PProject;

public interface PProjectDao {

	List<PProject> listAll();

	PProject findById(Integer id);

	void save(PProject project);

	void update(PProject project);

	void delete(Integer id);

}

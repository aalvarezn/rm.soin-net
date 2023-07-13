package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.Priority;

public interface PPriorityDao {

	List<Priority> list();

	Priority findById(Integer id);

	void save(Priority priority);

	void update(Priority priority);

	void delete(Integer id);

}

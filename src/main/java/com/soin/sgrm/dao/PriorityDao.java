package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.Priority;

public interface PriorityDao {

	List<Priority> list();

	Priority findById(Integer id);

	void save(Priority priority);

	void update(Priority priority);

	void delete(Integer id);

}

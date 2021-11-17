package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.Priority;

public interface PriorityService {

	List<Priority> list();

	Priority findById(Integer id);

	void save(Priority priority);

	void update(Priority priority);

	void delete(Integer id);

}

package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.Status;

public interface StatusDao {

	List<Status> list();
	
	Status findByName(String name);
	
	Status findById(Integer id);

	void save(Status status);

	void update(Status status);

	void delete(Integer id);
}

package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.Status;

public interface StatusService {
	
	List<Status> list();
	
	Status findByName(String name);
	
	Status findById(Integer id);

	void save(Status status);

	void update(Status status);

	void delete(Integer id);

	List<Status> listWithOutAnul();

}

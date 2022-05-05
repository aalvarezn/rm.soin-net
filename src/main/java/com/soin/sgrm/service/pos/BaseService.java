package com.soin.sgrm.service.pos;

import java.util.List;

public interface BaseService<PK, T> {

	T findById(PK id);

	T findByKey(String name, String value);

	List<T> findAll();

	void save(T model);

	void delete(Long id);

	void update(T model);

	
	

}

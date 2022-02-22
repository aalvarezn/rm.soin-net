package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.TypeAmbient;

public interface TypeAmbientService {
	
	List<TypeAmbient> list();
	
	TypeAmbient findByName(String name);
	
	TypeAmbient findById(Integer id);

	void save(TypeAmbient typeambient);

	void update(TypeAmbient typeambient);

	void delete(Integer id);

}

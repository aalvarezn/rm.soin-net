package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.TypeObject;

public interface TypeObjectService {

	List<TypeObject> listBySystem(Integer id);

	boolean existTypeObject(String name, Integer system_id);

	TypeObject findByName(String name, Integer system_id);

	TypeObject findByName(String name, String extension, Integer system_id);

	List<TypeObject> list();

	TypeObject findById(Integer id);

	TypeObject save(TypeObject type);

	void update(TypeObject typeObject);

	void delete(Integer id);

}

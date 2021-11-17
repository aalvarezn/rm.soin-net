package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.TypeObject;

public interface TypeObjectDao {

	List<TypeObject> listBySystem(Integer id);

	boolean existTypeObject(String name, Integer system_id);

	TypeObject save(TypeObject type);

	TypeObject findByName(String name, Integer system_id);

	TypeObject findByName(String name, String extension, Integer system_id);

	TypeObject findById(Integer id);

}
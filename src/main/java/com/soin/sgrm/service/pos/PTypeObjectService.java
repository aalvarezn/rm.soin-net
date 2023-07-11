package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PTypeObject;

public interface PTypeObjectService {

	List<PTypeObject> listBySystem(Integer id);

	boolean existTypeObject(String name, Integer system_id);

	PTypeObject findByName(String name, Integer system_id);

	PTypeObject findByName(String name, String extension, Integer system_id);

	List<PTypeObject> list();

	PTypeObject findById(Integer id);

	PTypeObject save(PTypeObject type);

	void update(PTypeObject typeObject);

	void delete(Integer id);

}

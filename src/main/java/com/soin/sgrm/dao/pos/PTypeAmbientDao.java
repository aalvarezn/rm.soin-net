package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.TypeAmbient;
import com.soin.sgrm.model.pos.PTypeAmbient;

public interface PTypeAmbientDao {

	List<PTypeAmbient> list();
	
	PTypeAmbient findByName(String name);
	
	PTypeAmbient findById(Integer id);

	void save(PTypeAmbient typeambient);

	void update(PTypeAmbient typeambient);

	void delete(Integer id);
}

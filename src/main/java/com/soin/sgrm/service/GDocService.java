package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.GDoc;

public interface GDocService {
	
	List<GDoc> list();

	GDoc findById(Integer id);

	void save(GDoc gDoc);

	void update(GDoc gDoc);

	void delete(Integer id);

}

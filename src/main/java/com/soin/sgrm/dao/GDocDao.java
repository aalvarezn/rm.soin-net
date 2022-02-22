package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.GDoc;

public interface GDocDao {

	List<GDoc> list();

	GDoc findById(Integer id);

	void save(GDoc gDoc);

	void update(GDoc gDoc);

	void delete(Integer id);

}

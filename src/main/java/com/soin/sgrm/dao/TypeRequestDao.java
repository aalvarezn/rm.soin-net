package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.TypeRequest;

public interface TypeRequestDao {

	List<TypeRequest> list();

	TypeRequest findById(Integer id);

	TypeRequest save(TypeRequest typeRequest);

	void update(TypeRequest typeRequest);

	void delete(Integer id);

}

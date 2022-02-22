package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.TypeRequest;

public interface TypeRequestService {

	List<TypeRequest> list();

	TypeRequest findById(Integer id);

	TypeRequest save(TypeRequest typeRequest);

	void update(TypeRequest typeRequest);

	void delete(Integer id);
}

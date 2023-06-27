package com.soin.sgrm.service.pos;

import java.sql.SQLException;
import java.util.List;

import com.soin.sgrm.model.pos.PRequest;
import com.soin.sgrm.model.pos.PTypeRequest;

public interface PRequestService {

	List<PRequest> list(String search, Object[] projects) throws SQLException;

	PRequest findByName(String code_soin, String code_ice);

	PRequest findByName(String code_soin);

	List<PRequest> list();

	List<PRequest> listByType(PTypeRequest type);
	
	PRequest listByTypeAndCodeSoin(PTypeRequest type,String code_soin);

	PRequest findById(Integer id);

	void save(PRequest request);

	void update(PRequest request);

	void delete(Integer id);
	
	void softDelete(PRequest request);

	PRequest findByNameCode(String tpo);



}

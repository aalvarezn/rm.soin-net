package com.soin.sgrm.dao.pos;

import java.sql.SQLException;
import java.util.List;

import com.soin.sgrm.model.Request;
import com.soin.sgrm.model.TypeRequest;
import com.soin.sgrm.model.pos.PRequest;
import com.soin.sgrm.model.pos.PTypeRequest;
import com.soin.sgrm.utils.JsonSheet;

public interface RequestDao {

	List<PRequest> list(String search, Object[] projects) throws SQLException;

	PRequest findByName(String code_soin, String code_ice);

	PRequest findByName(String code_soin);

	List<PRequest> list();

	PRequest findById(Integer id);

	void save(PRequest request);

	void update(PRequest request);

	void delete(Integer id);
	
	List<PRequest> listByType(PTypeRequest type);
	
	void softDelete(PRequest request);

	PRequest listByTypeAndCodeSoin(PTypeRequest type, String code_soin);

	PRequest findByNameCode(String code_soin);



}

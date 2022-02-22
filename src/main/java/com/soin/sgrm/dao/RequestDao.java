package com.soin.sgrm.dao;

import java.sql.SQLException;
import java.util.List;

import com.soin.sgrm.model.Request;
import com.soin.sgrm.model.TypeRequest;

public interface RequestDao {

	List<Request> list(String search, Object[] projects) throws SQLException;

	Request findByName(String code_soin, String code_ice);

	Request findByName(String code_soin);

	List<Request> list();

	Request findById(Integer id);

	void save(Request request);

	void update(Request request);

	void delete(Integer id);
	
	List<Request> listByType(TypeRequest type);
	
	void softDelete(Request request);

}

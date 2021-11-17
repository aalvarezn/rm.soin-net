package com.soin.sgrm.dao;

import java.sql.SQLException;
import java.util.List;

import com.soin.sgrm.model.Request;

public interface RequestDao {
	
	List<Request> list(String search) throws SQLException;
	
	Request findById(Integer id);
	
	Request findByName(String code_soin, String code_ice);
	
	Request findByName(String code_soin);

}

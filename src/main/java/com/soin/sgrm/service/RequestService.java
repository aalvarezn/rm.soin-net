package com.soin.sgrm.service;

import java.sql.SQLException;
import java.util.List;

import com.soin.sgrm.model.Request;
public interface RequestService {
	
	List<Request> list(String search) throws SQLException;
	
	Request findById(Integer id);
	
	Request findByName(String code_soin, String code_ice);
	
	Request findByName(String code_soin);

}

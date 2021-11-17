package com.soin.sgrm.service;

import java.sql.SQLException;
import java.util.List;

import com.soin.sgrm.model.ModifiedComponent;

public interface ModifiedComponentService {

	List<ModifiedComponent> list() throws SQLException;
	
	ModifiedComponent findById(Integer id);
}

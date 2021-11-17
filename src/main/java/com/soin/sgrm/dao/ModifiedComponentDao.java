package com.soin.sgrm.dao;

import java.sql.SQLException;
import java.util.List;

import com.soin.sgrm.model.ModifiedComponent;

public interface ModifiedComponentDao {
	
	List<ModifiedComponent> list() throws SQLException;
	
	ModifiedComponent findById(Integer id);

}

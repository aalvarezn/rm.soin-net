package com.soin.sgrm.dao;

import java.sql.SQLException;
import java.util.List;

import com.soin.sgrm.model.Ambient;

public interface AmbientDao {
	
	List<Ambient> list(Integer id);
	
	List<Ambient> list(String search, String system);
	
	Ambient findById(Integer id, String system);
	
	void addReleaseAmbient(Integer release_id, Integer ambient_id) throws SQLException;
	
	void deleteReleaseAmbient(Integer release_id, Integer ambient_id) throws SQLException;

}

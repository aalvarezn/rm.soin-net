package com.soin.sgrm.dao.pos;

import java.sql.SQLException;
import java.util.List;

import com.soin.sgrm.model.Ambient;
import com.soin.sgrm.model.pos.PAmbient;

public interface PAmbientDao {
	
	List<PAmbient> list();
	
	List<PAmbient> list(Integer id);
	
	List<PAmbient> list(String search, String system);
	
	PAmbient findById(Integer id, String system);
	
	void addReleaseAmbient(Integer release_id, Integer ambient_id) throws SQLException;
	
	void deleteReleaseAmbient(Integer release_id, Integer ambient_id) throws SQLException;
	
	PAmbient findById(Integer id);

	void save(PAmbient ambient);

	void update(PAmbient ambient);

	void delete(Integer id);

}

package com.soin.sgrm.service;

import java.sql.SQLException;
import java.util.List;

import com.soin.sgrm.model.Ambient;

public interface AmbientService {
	
	List<Ambient> list();

	List<Ambient> list(Integer id);

	List<Ambient> list(String search, String system);

	Ambient findById(Integer id, String system);

	void addReleaseAmbient(Integer release_id, Integer ambient_id) throws SQLException;

	void deleteReleaseAmbient(Integer release_id, Integer ambient_id) throws SQLException;

	Ambient findById(Integer id);

	void save(Ambient ambient);

	void update(Ambient ambient);

	void delete(Integer id);

}

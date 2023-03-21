package com.soin.sgrm.dao;

import com.soin.sgrm.model.IncidenceFile;


public interface IncidenceFileDao extends BaseDao<Long, IncidenceFile> {

	IncidenceFile findIncidenceFile(String path);

	void saveIncidenceFile(Long id, IncidenceFile incidenceFile) throws Exception;

	void deleteIncidence(IncidenceFile incidenceFile) throws Exception;
}

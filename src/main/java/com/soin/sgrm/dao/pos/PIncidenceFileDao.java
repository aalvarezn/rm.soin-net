package com.soin.sgrm.dao.pos;

import com.soin.sgrm.model.pos.PIncidenceFile;

public interface PIncidenceFileDao extends BaseDao<Long, PIncidenceFile> {

	PIncidenceFile findIncidenceFile(String path);

	void saveIncidenceFile(Long id, PIncidenceFile incidenceFile) throws Exception;

	void deleteIncidence(PIncidenceFile incidenceFile) throws Exception;
}

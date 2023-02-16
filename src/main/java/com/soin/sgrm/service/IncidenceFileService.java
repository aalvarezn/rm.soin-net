package com.soin.sgrm.service;

import com.soin.sgrm.model.IncidenceFile;

public interface IncidenceFileService extends BaseService<Long, IncidenceFile> {
	void saveIncidenceFile(Long id, IncidenceFile incidenceFile) throws Exception;
	void deleteIncidence(IncidenceFile incidenceFile) throws Exception;
}

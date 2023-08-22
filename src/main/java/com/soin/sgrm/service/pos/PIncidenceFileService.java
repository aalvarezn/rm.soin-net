package com.soin.sgrm.service.pos;

import com.soin.sgrm.model.pos.PIncidenceFile;

public interface PIncidenceFileService extends BaseService<Long, PIncidenceFile> {
	void saveIncidenceFile(Long id, PIncidenceFile incidenceFile) throws Exception;
	void deleteIncidence(PIncidenceFile incidenceFile) throws Exception;
}

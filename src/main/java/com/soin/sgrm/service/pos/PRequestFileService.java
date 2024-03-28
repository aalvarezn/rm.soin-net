package com.soin.sgrm.service.pos;

import com.soin.sgrm.model.RequestBaseFile;
import com.soin.sgrm.model.pos.PRequestBaseFile;

public interface PRequestFileService extends BaseService<Long, PRequestBaseFile> {
	void saveRequestFile(Long id, PRequestBaseFile requestBaseFile) throws Exception;
	void deleteRequest(PRequestBaseFile requestBaseFile) throws Exception;
}

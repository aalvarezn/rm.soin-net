package com.soin.sgrm.service.pos;

import com.soin.sgrm.model.RFCFile;

public interface PRFCFileService extends BaseService<Long, RFCFile> {
	void saveRFCFile(Long id, RFCFile rfcFile) throws Exception;
	void deleteRFC(RFCFile rfcFile) throws Exception;
}

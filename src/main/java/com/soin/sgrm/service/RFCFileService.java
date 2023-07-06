package com.soin.sgrm.service;

import com.soin.sgrm.model.RFCFile;

public interface RFCFileService extends BaseService<Long, RFCFile> {
	void saveRFCFile(Long id, RFCFile rfcFile) throws Exception;
	void deleteRFC(RFCFile rfcFile) throws Exception;
}

package com.soin.sgrm.service.pos;


import com.soin.sgrm.model.pos.PRFCFile;

public interface RFCFileService extends BaseService<Long, PRFCFile> {
	
	void saveRFCFile(Long id, PRFCFile rfcFile) throws Exception;
	void deleteRFC(PRFCFile rfcFile) throws Exception;
}

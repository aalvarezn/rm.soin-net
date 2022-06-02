package com.soin.sgrm.dao;

import com.soin.sgrm.model.RFCFile;

public interface RFCFileDao extends BaseDao<Long, RFCFile> {

	RFCFile findRFCFile(String path);

	void saveRFCFile(Long id, RFCFile rfcFile) throws Exception;

	void deleteRFC(RFCFile rfcFile) throws Exception;
}

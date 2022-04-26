package com.soin.sgrm.dao.pos;

import com.soin.sgrm.model.pos.PRFCFile;

public interface RFCFileDao extends BaseDao<Long, PRFCFile>{

	PRFCFile findRFCFile(String path);

	void saveRFCFile(Long id, PRFCFile rfcFile) throws Exception;

	void deleteRFC(PRFCFile rfcFile) throws Exception;

}

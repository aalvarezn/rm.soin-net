package com.soin.sgrm.service.pos;

import com.soin.sgrm.model.ReleaseFile;
import com.soin.sgrm.model.pos.PRFCFile;

public interface RFCFileService extends BaseService<Long, PRFCFile> {
	void save(Integer id, PRFCFile rfcFile) throws Exception;
}

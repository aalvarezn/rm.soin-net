package com.soin.sgrm.service;

import com.soin.sgrm.model.RequestBaseFile;

public interface RequestFileService extends BaseService<Long, RequestBaseFile> {
	void saveRequestFile(Long id, RequestBaseFile requestBaseFile) throws Exception;
	void deleteRequest(RequestBaseFile requestBaseFile) throws Exception;
}

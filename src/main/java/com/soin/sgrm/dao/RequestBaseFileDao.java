package com.soin.sgrm.dao;

import com.soin.sgrm.model.RequestBaseFile;

public interface RequestBaseFileDao extends BaseDao<Long, RequestBaseFile> {

	RequestBaseFile findRequestFile(String path);

	void saveRequestFile(Long id, RequestBaseFile requestFile) throws Exception;

	void deleteRequest(RequestBaseFile requestFile) throws Exception;
}

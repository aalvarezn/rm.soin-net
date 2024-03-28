package com.soin.sgrm.dao.pos;

import com.soin.sgrm.model.pos.PRequestBaseFile;

public interface PRequestBaseFileDao extends BaseDao<Long, PRequestBaseFile> {

	PRequestBaseFile findRequestFile(String path);

	void saveRequestFile(Long id, PRequestBaseFile requestFile) throws Exception;

	void deleteRequest(PRequestBaseFile requestFile) throws Exception;
}

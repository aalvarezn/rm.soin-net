package com.soin.sgrm.dao;

import com.soin.sgrm.model.BaseKnowledgeFile;

public interface BaseKnowledgeFileDao extends BaseDao<Long, BaseKnowledgeFile> {

	BaseKnowledgeFile findBaseKnowledgeFile(String path);

	void saveBaseKnowledgeFile(Long id, BaseKnowledgeFile baseKnowledgeFile) throws Exception;

	void deleteBaseKnowLedgeFile(BaseKnowledgeFile baseKnowledgeFile) throws Exception;
}

package com.soin.sgrm.service;

import com.soin.sgrm.model.BaseKnowledgeFile;

public interface BaseKnowledgeFileService extends BaseService<Long, BaseKnowledgeFile> {
	void saveBaseKnowledgeFile(Long id, BaseKnowledgeFile baseKnowledgeFile) throws Exception;
	void deleteBaseKnowLedgeFile(BaseKnowledgeFile baseKnowledgeFile) throws Exception;
}

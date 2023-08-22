package com.soin.sgrm.service.pos;

import com.soin.sgrm.model.pos.PBaseKnowledgeFile;

public interface PBaseKnowledgeFileService extends BaseService<Long, PBaseKnowledgeFile> {
	void saveBaseKnowledgeFile(Long id, PBaseKnowledgeFile baseKnowledgeFile) throws Exception;
	void deleteBaseKnowLedgeFile(PBaseKnowledgeFile baseKnowledgeFile) throws Exception;
}

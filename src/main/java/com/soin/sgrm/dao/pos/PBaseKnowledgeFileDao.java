package com.soin.sgrm.dao.pos;


import com.soin.sgrm.model.pos.PBaseKnowledgeFile;

public interface PBaseKnowledgeFileDao extends BaseDao<Long, PBaseKnowledgeFile> {

	PBaseKnowledgeFile findBaseKnowledgeFile(String path);

	void saveBaseKnowledgeFile(Long id, PBaseKnowledgeFile baseKnowledgeFile) throws Exception;

	void deleteBaseKnowLedgeFile(PBaseKnowledgeFile baseKnowledgeFile) throws Exception;
}

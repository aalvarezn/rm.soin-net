package com.soin.sgrm.service.pos;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.BaseKnowledgeFileDao;
import com.soin.sgrm.dao.pos.PBaseKnowledgeFileDao;
import com.soin.sgrm.model.BaseKnowledgeFile;
import com.soin.sgrm.model.pos.PBaseKnowledgeFile;

@Service("PBaseKnowledgeFileService")
@Transactional("transactionManagerPos")
public class PBaseKnowledgeFileServiceImpl implements PBaseKnowledgeFileService {
	
	@Autowired
	PBaseKnowledgeFileDao dao;
	@Override
	public PBaseKnowledgeFile findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public PBaseKnowledgeFile findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<PBaseKnowledgeFile> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(PBaseKnowledgeFile model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PBaseKnowledgeFile model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PBaseKnowledgeFile model) {
		dao.update(model);
	}

	
	@Override
	public void saveBaseKnowledgeFile(Long id, PBaseKnowledgeFile BaseKnowledgeFile) throws Exception{
		dao.saveBaseKnowledgeFile(id,BaseKnowledgeFile);
	}

	@Override
	public void deleteBaseKnowLedgeFile(PBaseKnowledgeFile BaseKnowledgeFile) throws Exception {
		dao.deleteBaseKnowLedgeFile(BaseKnowledgeFile);
	}

}

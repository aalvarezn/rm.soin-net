package com.soin.sgrm.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.BaseKnowledgeFileDao;
import com.soin.sgrm.model.BaseKnowledgeFile;

@Service("BaseKnowledgeFileService")
@Transactional("transactionManager")
public class BaseKnowledgeFileServiceImpl implements BaseKnowledgeFileService {
	
	@Autowired
	BaseKnowledgeFileDao dao;
	@Override
	public BaseKnowledgeFile findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public BaseKnowledgeFile findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<BaseKnowledgeFile> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(BaseKnowledgeFile model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		BaseKnowledgeFile model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(BaseKnowledgeFile model) {
		dao.update(model);
	}

	
	@Override
	public void saveBaseKnowledgeFile(Long id, BaseKnowledgeFile BaseKnowledgeFile) throws Exception{
		dao.saveBaseKnowledgeFile(id,BaseKnowledgeFile);
	}

	@Override
	public void deleteBaseKnowLedgeFile(BaseKnowledgeFile BaseKnowledgeFile) throws Exception {
		dao.deleteBaseKnowLedgeFile(BaseKnowledgeFile);
	}

}

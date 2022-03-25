package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.DocumentDao;
import com.soin.sgrm.model.pos.PDocument;

@Service("documentService")
@Transactional("transactionManagerPos")
public class DocumentServiceImpl implements DocumentService{
    @Autowired
    DocumentDao dao;
	
	@Override
	public PDocument findById(Long id) {

		return dao.getById(id);
	}

	@Override
	public PDocument findByKey(String name, String value) {

		return dao.getByKey(name, value);
	}

	@Override
	public List<PDocument> findAll() {

		return dao.findAll();
	}

	@Override
	public void save(PDocument model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PDocument model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PDocument model) {
		dao.update(model);
	}

}

package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.model.pos.PDocument;

@Service("documentService")
@Transactional("transactionManagerPos")
public class DocumentServiceImpl implements DocumentService{

	@Override
	public PDocument findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PDocument findByKey(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PDocument> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(PDocument model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PDocument model) {
		// TODO Auto-generated method stub
		
	}

}

package com.soin.sgrm.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.RFCFileDao;
import com.soin.sgrm.dao.RequestBaseFileDao;
import com.soin.sgrm.model.RFCFile;
import com.soin.sgrm.model.RequestBaseFile;

@Service("RequestFileService")
@Transactional("transactionManager")
public class RequestFileServiceImpl implements RequestFileService {
	
	@Autowired
	RequestBaseFileDao dao;
	
	@Override
	public RequestBaseFile findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public RequestBaseFile findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public void save(RequestBaseFile model) {
		dao.save(model);
	}

	@Override
	public void update(RequestBaseFile model) {
		dao.update(model);
	}

	@Override
	public void saveRequestFile(Long id, RequestBaseFile requestBaseFile) throws Exception {
		dao.saveRequestFile(id, requestBaseFile);
	}

	@Override
	public void deleteRequest(RequestBaseFile requestBaseFile) throws Exception {
		dao.deleteRequest(requestBaseFile);
	}

	@Override
	public List<RequestBaseFile> findAll() {
		return dao.findAll();
	}


	@Override
	public void delete(Long id) {
		RequestBaseFile model= findById(id);
		dao.delete(model);
	}

}

package com.soin.sgrm.service.pos;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.RFCFileDao;
import com.soin.sgrm.dao.RequestBaseFileDao;
import com.soin.sgrm.dao.pos.PRequestBaseFileDao;
import com.soin.sgrm.model.pos.PRequestBaseFile;

@Service("PRequestFileService")
@Transactional("transactionManagerPos")
public class PRequestFileServiceImpl implements PRequestFileService {
	
	@Autowired
	PRequestBaseFileDao dao;
	
	@Override
	public PRequestBaseFile findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PRequestBaseFile findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public void save(PRequestBaseFile model) {
		dao.save(model);
	}

	@Override
	public void update(PRequestBaseFile model) {
		dao.update(model);
	}

	@Override
	public void saveRequestFile(Long id, PRequestBaseFile requestBaseFile) throws Exception {
		dao.saveRequestFile(id, requestBaseFile);
	}

	@Override
	public void deleteRequest(PRequestBaseFile requestBaseFile) throws Exception {
		dao.deleteRequest(requestBaseFile);
	}

	@Override
	public List<PRequestBaseFile> findAll() {
		return dao.findAll();
	}


	@Override
	public void delete(Long id) {
		PRequestBaseFile model= findById(id);
		dao.delete(model);
	}

}

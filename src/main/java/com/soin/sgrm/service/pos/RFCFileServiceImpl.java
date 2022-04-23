package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.RFCFileDao;
import com.soin.sgrm.model.pos.PRFCFile;

@Service("rfcFileService")
@Transactional("transactionManagerPos")
public class RFCFileServiceImpl implements RFCFileService {
	
	RFCFileDao dao;
	@Override
	public PRFCFile findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public PRFCFile findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<PRFCFile> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(PRFCFile model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PRFCFile model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PRFCFile model) {
		dao.update(model);
	}

	@Override
	public void save(Integer id, PRFCFile rfcFile) throws Exception {
		// TODO Auto-generated method stub
		
	}

}

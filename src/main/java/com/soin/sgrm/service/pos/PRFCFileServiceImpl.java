package com.soin.sgrm.service.pos;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.RFCFileDao;
import com.soin.sgrm.model.RFCFile;

@Service("RFCFileService")
@Transactional("transactionManager")
public class PRFCFileServiceImpl implements PRFCFileService {
	
	@Autowired
	RFCFileDao dao;
	@Override
	public RFCFile findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public RFCFile findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<RFCFile> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(RFCFile model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		RFCFile model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(RFCFile model) {
		dao.update(model);
	}

	
	@Override
	public void saveRFCFile(Long id, RFCFile rfcFile) throws Exception{
		dao.saveRFCFile(id,rfcFile);
	}

	@Override
	public void deleteRFC(RFCFile rfcFile) throws Exception {
		dao.deleteRFC(rfcFile);
	}

}

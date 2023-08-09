package com.soin.sgrm.service.pos;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PIncidenceFileDao;
import com.soin.sgrm.model.pos.PIncidenceFile;

@Service("PIncidenceFileService")
@Transactional("transactionManagerPos")
public class PIncidenceFileServiceImpl implements PIncidenceFileService {
	
	@Autowired
	PIncidenceFileDao dao;
	@Override
	public PIncidenceFile findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public PIncidenceFile findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<PIncidenceFile> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(PIncidenceFile model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PIncidenceFile model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PIncidenceFile model) {
		dao.update(model);
	}

	
	@Override
	public void saveIncidenceFile(Long id, PIncidenceFile incidenceFile) throws Exception{
		dao.saveIncidenceFile(id,incidenceFile);
	}

	@Override
	public void deleteIncidence(PIncidenceFile incidenceFile) throws Exception {
		dao.deleteIncidence(incidenceFile);
	}

}

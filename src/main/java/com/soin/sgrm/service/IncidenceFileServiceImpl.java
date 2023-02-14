package com.soin.sgrm.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.IncidenceFileDao;
import com.soin.sgrm.model.IncidenceFile;

@Service("IncidenceFileService")
@Transactional("transactionManager")
public class IncidenceFileServiceImpl implements IncidenceFileService {
	
	@Autowired
	IncidenceFileDao dao;
	@Override
	public IncidenceFile findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public IncidenceFile findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<IncidenceFile> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(IncidenceFile model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		IncidenceFile model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(IncidenceFile model) {
		dao.update(model);
	}

	
	@Override
	public void saveIncidenceFile(Long id, IncidenceFile incidenceFile) throws Exception{
		dao.saveIncidenceFile(id,incidenceFile);
	}

	@Override
	public void deleteIncidence(IncidenceFile incidenceFile) throws Exception {
		dao.deleteIncidence(incidenceFile);
	}

}

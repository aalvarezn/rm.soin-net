package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.StatusIncidenceDao;
import com.soin.sgrm.dao.StatusRequestDao;
import com.soin.sgrm.model.StatusIncidence;
import com.soin.sgrm.model.StatusRequest;

@Transactional("transactionManager")
@Service("StatusIncidenceService")
public class StatusIncidenceServiceImpl implements StatusIncidenceService{

	@Autowired
	StatusIncidenceDao dao;
	
	@Override
	public StatusIncidence findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public StatusIncidence findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<StatusIncidence> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(StatusIncidence model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		StatusIncidence model=findById(id);
		dao.delete(model);
		
	}

	@Override
	public void update(StatusIncidence model) {
		dao.update(model);
	}

}

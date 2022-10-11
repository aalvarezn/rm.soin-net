package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.StatusRequestDao;
import com.soin.sgrm.model.StatusRequest;

@Transactional("transactionManager")
@Service("StatusRequestService")
public class StatusRequestServiceImpl implements StatusRequestService{

	@Autowired
	StatusRequestDao dao;
	
	@Override
	public StatusRequest findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public StatusRequest findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<StatusRequest> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(StatusRequest model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		StatusRequest model=findById(id);
		dao.delete(model);
		
	}

	@Override
	public void update(StatusRequest model) {
		dao.update(model);
	}

}

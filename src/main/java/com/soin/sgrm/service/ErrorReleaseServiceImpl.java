package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ErrorReleaseDao;
import com.soin.sgrm.model.Errors_Release;

@Transactional("transactionManager")
@Service("ErrorReleaseService")
public class ErrorReleaseServiceImpl  implements ErrorReleaseService{
	@Autowired
	ErrorReleaseDao dao;
	
	@Override
	public Errors_Release findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public Errors_Release findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<Errors_Release> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(Errors_Release model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		Errors_Release model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(Errors_Release model) {
		dao.update(model);
	}

}
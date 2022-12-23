package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ErrorRFCDao;
import com.soin.sgrm.model.Errors_RFC;

@Transactional("transactionManager")
@Service("ErrorRFCService")
public class ErrorRFCServiceImpl  implements ErrorRFCService{
	@Autowired
	ErrorRFCDao dao;
	
	@Override
	public Errors_RFC findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public Errors_RFC findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<Errors_RFC> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(Errors_RFC model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		Errors_RFC model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(Errors_RFC model) {
		dao.update(model);
	}

}
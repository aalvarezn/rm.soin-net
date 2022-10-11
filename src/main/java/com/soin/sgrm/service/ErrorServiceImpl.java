package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ErrorDao;
import com.soin.sgrm.model.Errors;

@Transactional("transactionManager")
@Service("ErrorService")
public class ErrorServiceImpl  implements ErrorService{
	@Autowired
	ErrorDao dao;
	
	@Override
	public Errors findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public Errors findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<Errors> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(Errors model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		Errors model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(Errors model) {
		dao.update(model);
	}

}

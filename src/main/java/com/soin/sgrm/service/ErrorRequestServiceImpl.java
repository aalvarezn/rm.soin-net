package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ErrorRequestDao;
import com.soin.sgrm.model.Errors_Requests;


@Transactional("transactionManager")
@Service("ErrorRequestService")
public class ErrorRequestServiceImpl  implements ErrorRequestService{
	@Autowired
	ErrorRequestDao dao;
	
	@Override
	public Errors_Requests findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public Errors_Requests findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<Errors_Requests> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(Errors_Requests model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		Errors_Requests model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(Errors_Requests model) {
		dao.update(model);
	}

}
package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.TypeChangeDao;
import com.soin.sgrm.model.TypeChange;

@Transactional("transactionManager")
@Service("TypeChangeService")
public class TypeChangeServiceImpl implements TypeChangeService {
	
	@Autowired
	TypeChangeDao dao;
	
	@Override
	public TypeChange findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public TypeChange findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public List<TypeChange> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(TypeChange model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		TypeChange model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(TypeChange model) {
		dao.update(model);
	}

}

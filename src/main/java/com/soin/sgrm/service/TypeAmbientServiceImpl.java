package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.TypeAmbientDao;
import com.soin.sgrm.model.TypeAmbient;

@Transactional("transactionManager")
@Service("TypeAmbientService")
public class TypeAmbientServiceImpl implements TypeAmbientService {

	@Autowired
	TypeAmbientDao dao;

	@Override
	public List<TypeAmbient> list() {
		return dao.list();
	}

	@Override
	public TypeAmbient findByName(String name) {
		return dao.findByName(name);
	}

	@Override
	public TypeAmbient findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void save(TypeAmbient typeambient) {
		dao.save(typeambient);
	}

	@Override
	public void update(TypeAmbient typeambient) {
		dao.update(typeambient);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

}

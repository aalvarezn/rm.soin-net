package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.TypeObjectDao;
import com.soin.sgrm.model.TypeObject;

@Transactional("transactionManager")
@Service("TypeObjectService")
public class TypeObjectServiceImpl implements TypeObjectService {

	@Autowired
	TypeObjectDao dao;

	@Override
	public List<TypeObject> listBySystem(Integer id) {
		return dao.listBySystem(id);
	}

	@Override
	public boolean existTypeObject(String name, Integer system_id) {
		return dao.existTypeObject(name, system_id);
	}

	@Override
	public TypeObject findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public TypeObject findByName(String name, String extension, Integer system_id) {
		return dao.findByName(name, extension, system_id);
	}

	@Override
	public TypeObject findByName(String name, Integer system_id) {
		return dao.findByName(name, "", system_id);
	}

	@Override
	public TypeObject save(TypeObject type) {
		return dao.save(type);
	}

	@Override
	public List<TypeObject> list() {
		return dao.list();
	}

	@Override
	public void update(TypeObject typeObject) {
		dao.update(typeObject);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

}

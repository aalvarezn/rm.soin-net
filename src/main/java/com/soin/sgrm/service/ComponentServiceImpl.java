package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ComponentDao;
import com.soin.sgrm.model.Component;

@Transactional("transactionManager")
@Service("ComponentService")
public class ComponentServiceImpl  implements ComponentService{
	@Autowired
	ComponentDao dao;
	
	@Override
	public Component findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public Component findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<Component> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(Component model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		Component model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(Component model) {
		dao.update(model);
	}

}

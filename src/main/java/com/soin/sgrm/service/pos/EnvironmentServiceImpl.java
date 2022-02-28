package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.soin.sgrm.dao.pos.EnvironmentDao;
import com.soin.sgrm.model.pos.PEnvironment;

public class EnvironmentServiceImpl implements EnvironmentService{

	@Autowired
	EnvironmentDao dao;
	
	@Override
	public PEnvironment findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PEnvironment findByKey(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PEnvironment> findAll() {
		// TODO Auto-generated method stub
		return dao.findAll();
	}

	@Override
	public void save(PEnvironment model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PEnvironment model) {
		// TODO Auto-generated method stub
		
	}

}

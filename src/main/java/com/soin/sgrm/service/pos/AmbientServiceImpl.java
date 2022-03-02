package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.soin.sgrm.dao.pos.AmbientDao;
import com.soin.sgrm.model.pos.PAmbient;

public class AmbientServiceImpl implements AmbientService{

	@Autowired
	AmbientDao dao;
	
	@Override
	public PAmbient findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PAmbient findByKey(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PAmbient> findAll() {
		// TODO Auto-generated method stub
		return dao.findAll();
	}

	@Override
	public void save(PAmbient model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PAmbient model) {
		// TODO Auto-generated method stub
		
	}

}

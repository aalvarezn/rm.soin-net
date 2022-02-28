package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.soin.sgrm.dao.pos.TypeChangeDao;
import com.soin.sgrm.model.pos.PTypeChange;

public class TypeChangeServiceImpl implements TypeChangeService{
	
	@Autowired
	TypeChangeDao dao;
	@Override
	public PTypeChange findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PTypeChange findByKey(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PTypeChange> findAll() {
		// TODO Auto-generated method stub
		return dao.findAll();
	}

	@Override
	public void save(PTypeChange model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PTypeChange model) {
		// TODO Auto-generated method stub
		
	}

}

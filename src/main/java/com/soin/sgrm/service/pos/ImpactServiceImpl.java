package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.soin.sgrm.dao.pos.ImpactDao;
import com.soin.sgrm.model.pos.PImpact;

public class ImpactServiceImpl implements ImpactService {
	@Autowired
	ImpactDao dao;
	@Override
	public PImpact findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PImpact findByKey(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PImpact> findAll() {
		// TODO Auto-generated method stub
		return dao.findAll();
	}

	@Override
	public void save(PImpact model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PImpact model) {
		// TODO Auto-generated method stub
		
	}

}

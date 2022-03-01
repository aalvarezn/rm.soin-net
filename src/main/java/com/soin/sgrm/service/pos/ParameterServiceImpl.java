package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.soin.sgrm.model.pos.PParameter;

public class ParameterServiceImpl implements ParameterService {
	@Autowired
	ParameterService dao;
	@Override
	public PParameter findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PParameter findByKey(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PParameter> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PParameter model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PParameter model) {
		// TODO Auto-generated method stub
		
	}

}

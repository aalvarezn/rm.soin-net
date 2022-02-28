package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.soin.sgrm.dao.pos.TypeRequestDao;
import com.soin.sgrm.model.pos.PTypeRequest;

public class TypeRequestServiceImpl implements TypeRequestService{
	@Autowired
	TypeRequestDao dao;
	
	@Override
	public PTypeRequest findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PTypeRequest findByKey(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PTypeRequest> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PTypeRequest model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PTypeRequest model) {
		// TODO Auto-generated method stub
		
	}

}

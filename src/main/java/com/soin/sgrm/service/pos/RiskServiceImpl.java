package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.soin.sgrm.dao.pos.RiskDao;
import com.soin.sgrm.model.pos.PRisk;

public class RiskServiceImpl implements RiskService {
	@Autowired 
	RiskDao dao;
	
	@Override
	public PRisk findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PRisk findByKey(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PRisk> findAll() {
		// TODO Auto-generated method stub
		return dao.findAll();
	}

	@Override
	public void save(PRisk model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PRisk model) {
		// TODO Auto-generated method stub
		
	}

}

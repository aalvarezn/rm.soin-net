package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.soin.sgrm.dao.pos.StatusDao;
import com.soin.sgrm.model.pos.PStatus;

@Service("statusService")
@Transactional("transactionManagerPos")
public class StatusServiceImpl implements StatusService {
	
	@Autowired
	StatusDao dao;
	@Override
	public PStatus findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PStatus findByKey(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PStatus> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PStatus model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PStatus model) {
		// TODO Auto-generated method stub
		
	}

}

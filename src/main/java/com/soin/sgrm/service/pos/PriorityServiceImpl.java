package com.soin.sgrm.service.pos;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PriorityDao;
import com.soin.sgrm.model.pos.PPriority;

@Service("priorityService")
@Transactional("transactionManagerPos")
public class PriorityServiceImpl implements PriorityService {

	@Autowired
	PriorityDao dao;

	@Override
	public PPriority findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PPriority findByKey(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PPriority> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PPriority model) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(PPriority model) {
		// TODO Auto-generated method stub

	}

}

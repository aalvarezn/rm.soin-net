package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.PriorityDao;
import com.soin.sgrm.model.Priority;

@Transactional("transactionManager")
@Service("PriorityService")
public class PriorityServiceImpl implements PriorityService {

	@Autowired
	PriorityDao dao;

	@Override
	public List<Priority> list() {
		return dao.list();
	}

	@Override
	public Priority findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void save(Priority priority) {
		dao.save(priority);
	}

	@Override
	public void update(Priority priority) {
		dao.update(priority);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

}

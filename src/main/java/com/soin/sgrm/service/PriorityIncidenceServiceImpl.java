package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.PriorityDao;
import com.soin.sgrm.dao.PriorityIncidenceDao;
import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.PriorityIncidence;

@Transactional("transactionManager")
@Service("PriorityIncidenceService")
public class PriorityIncidenceServiceImpl implements PriorityIncidenceService {

	@Autowired
	PriorityIncidenceDao dao;

	@Override
	public PriorityIncidence findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PriorityIncidence findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public List<PriorityIncidence> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PriorityIncidence model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PriorityIncidence model= findById(id);
		dao.delete(model);
		
	}

	@Override
	public void update(PriorityIncidence model) {
		dao.update(model);
	}


}


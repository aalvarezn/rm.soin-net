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
		return dao.getById(id);
	}

	@Override
	public PPriority findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public List<PPriority> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PPriority model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PPriority model = findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PPriority model) {
		dao.update(model);
	}

}

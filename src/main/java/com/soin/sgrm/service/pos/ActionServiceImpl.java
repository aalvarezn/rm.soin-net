package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.ActionDao;
import com.soin.sgrm.model.pos.PAction;

@Service("actionService")
@Transactional("transactionManagerPos")
public class ActionServiceImpl implements ActionService{
	@Autowired
	ActionDao dao;
	
	@Override
	public PAction findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PAction findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public List<PAction> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PAction model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		
		PAction model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PAction model) {
		dao.update(model);
	}

}

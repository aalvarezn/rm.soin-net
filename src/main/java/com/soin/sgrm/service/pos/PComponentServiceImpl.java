package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ComponentDao;
import com.soin.sgrm.dao.pos.PComponentDao;
import com.soin.sgrm.model.Component;
import com.soin.sgrm.model.pos.PComponent;

@Transactional("transactionManagerPos")
@Service("PComponentService")
public class PComponentServiceImpl  implements PComponentService{
	@Autowired
	PComponentDao dao;
	
	@Override
	public PComponent findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public PComponent findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<PComponent> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(PComponent model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PComponent model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PComponent model) {
		dao.update(model);
	}

	@Override
	public List<PComponent> findBySystem(List<Integer> systemIds) {
		return dao.findBySystem(systemIds);
	}

	@Override
	public List<PComponent> findBySystem(Integer id) {
		return dao.findBySystem(id);
	}

}

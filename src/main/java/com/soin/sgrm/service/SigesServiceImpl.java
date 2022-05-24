package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.SigesDao;
import com.soin.sgrm.model.Siges;

@Service("sigesService")
@Transactional("transactionManager")
public class SigesServiceImpl implements SigesService{

	@Autowired
	SigesDao dao;
	
	@Override
	public Siges findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public Siges findByKey(String name, String value) {

		return dao.getByKey(name, value);
	}

	@Override
	public List<Siges> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(Siges model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		Siges model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(Siges model) {
		dao.update(model);
	}

	@Override
	public List<Siges> listCodeSiges(Long id) {
		return dao.listCodeSiges(id);
	}

}

package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.StatusKnowlegeDao;
import com.soin.sgrm.model.StatusKnowlege;

@Transactional("transactionManager")
@Service("StatusKnowlegeService")
public class StatusKnowlegeServiceImpl  implements StatusKnowlegeService{
	@Autowired
	StatusKnowlegeDao dao;
	
	@Override
	public StatusKnowlege findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public StatusKnowlege findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<StatusKnowlege> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(StatusKnowlege model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		StatusKnowlege model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(StatusKnowlege model) {
		dao.update(model);
	}

}

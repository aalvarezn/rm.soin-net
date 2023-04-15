package com.soin.sgrm.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ReleaseCountDao;
import com.soin.sgrm.model.ReleaseCount;

@Transactional("transactionManager")
@Service("ReleaseCountServiceService")
public class ReleaseCountServiceImpl  implements ReleaseCountService{
	@Autowired
	ReleaseCountDao dao;
	
	@Override
	public ReleaseCount findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public ReleaseCount findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<ReleaseCount> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(ReleaseCount model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		ReleaseCount model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(ReleaseCount model) {
		dao.update(model);
	}

	@Override
	public List<ReleaseCount> findAllList(String[] releaseName, String status, String dateRange) {
		// TODO Auto-generated method stub
		return dao.findAllList(releaseName, status, dateRange);
	}

	
}
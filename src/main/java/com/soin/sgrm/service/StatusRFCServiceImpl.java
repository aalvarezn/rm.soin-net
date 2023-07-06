package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.StatusRFCDao;
import com.soin.sgrm.model.StatusRFC;

@Transactional("transactionManager")
@Service("StatusRFCService")
public class StatusRFCServiceImpl  implements StatusRFCService{
	@Autowired
	StatusRFCDao dao;
	
	@Override
	public StatusRFC findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public StatusRFC findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<StatusRFC> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(StatusRFC model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		StatusRFC model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(StatusRFC model) {
		dao.update(model);
	}

	@Override
	public List<StatusRFC> findWithFilter() {
		
		return dao.findWithFilter();
	}

}

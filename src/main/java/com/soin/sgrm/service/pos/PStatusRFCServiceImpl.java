package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.StatusRFCDao;
import com.soin.sgrm.dao.pos.PStatusRFCDao;
import com.soin.sgrm.model.StatusRFC;
import com.soin.sgrm.model.pos.PStatusRFC;

@Transactional("transactionManagerPos")
@Service("PStatusRFCService")
public class PStatusRFCServiceImpl  implements PStatusRFCService{
	@Autowired
	PStatusRFCDao dao;
	
	@Override
	public PStatusRFC findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public PStatusRFC findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<PStatusRFC> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(PStatusRFC model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PStatusRFC model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PStatusRFC model) {
		dao.update(model);
	}

	@Override
	public List<PStatusRFC> findWithFilter() {
		
		return dao.findWithFilter();
	}

}

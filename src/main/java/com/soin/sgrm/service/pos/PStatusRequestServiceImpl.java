package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.StatusRequestDao;
import com.soin.sgrm.dao.pos.PStatusRequestDao;
import com.soin.sgrm.model.StatusRequest;
import com.soin.sgrm.model.pos.PStatusRequest;

@Transactional("transactionManagerPos")
@Service("PStatusRequestService")
public class PStatusRequestServiceImpl implements PStatusRequestService{

	@Autowired
	PStatusRequestDao dao;
	
	@Override
	public PStatusRequest findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public PStatusRequest findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<PStatusRequest> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(PStatusRequest model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PStatusRequest model=findById(id);
		dao.delete(model);
		
	}

	@Override
	public void update(PStatusRequest model) {
		dao.update(model);
	}

	@Override
	public List<PStatusRequest> findWithFilter() {
		
		return dao.findWithFilter();
	}

}

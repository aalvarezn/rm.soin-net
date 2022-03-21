package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.TypeRequestDao;
import com.soin.sgrm.model.pos.PTypeRequest;

@Service("typeRequestService")
@Transactional("transactionManagerPos")
public class TypeRequestServiceImpl implements TypeRequestService{
	@Autowired
	TypeRequestDao dao;
	
	@Override
	public PTypeRequest findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PTypeRequest findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public List<PTypeRequest> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PTypeRequest model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PTypeRequest model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PTypeRequest model) {
		dao.update(model);
	}

}

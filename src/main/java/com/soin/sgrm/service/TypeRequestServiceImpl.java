package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.TypeRequestDao;
import com.soin.sgrm.model.TypeRequest;

@Transactional("transactionManager")
@Service("TypeRequestService")
public class TypeRequestServiceImpl implements TypeRequestService {

	@Autowired
	TypeRequestDao dao;

	@Override
	public List<TypeRequest> list() {
		return dao.list();
	}

	@Override
	public TypeRequest findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public TypeRequest save(TypeRequest typeRequest) {
		return dao.save(typeRequest);
	}

	@Override
	public void update(TypeRequest typeRequest) {
		dao.update(typeRequest);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

}

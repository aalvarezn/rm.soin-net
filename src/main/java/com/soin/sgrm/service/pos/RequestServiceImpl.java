package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.RequestDao;
import com.soin.sgrm.model.pos.PRequest;

@Service("requestService")
@Transactional("transactionManagerPos")
public class RequestServiceImpl implements RequestService {
	@Autowired
	RequestDao dao;
	@Override
	public PRequest findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PRequest findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public List<PRequest> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PRequest model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PRequest model = findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PRequest model) {
		dao.update(model);
	}

}

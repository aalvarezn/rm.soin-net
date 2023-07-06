package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.Request_EstimateDao;
import com.soin.sgrm.model.Request_Estimate;

@Service("Request_EstimateService")
@Transactional("transactionManager")
public class Request_EstimateServiceImpl implements Request_EstimateService{

	@Autowired
	Request_EstimateDao dao;
	
	@Override
	public Request_Estimate findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public Request_Estimate findByKey(String name, String value) {

		return dao.getByKey(name, value);
	}

	@Override
	public List<Request_Estimate> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(Request_Estimate model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		Request_Estimate model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(Request_Estimate model) {
		dao.update(model);
	}

	@Override
	public Request_Estimate findByIdRequest(Long idRequest) {
		// TODO Auto-generated method stub
		return dao.findByIdRequest(idRequest);
	}


}

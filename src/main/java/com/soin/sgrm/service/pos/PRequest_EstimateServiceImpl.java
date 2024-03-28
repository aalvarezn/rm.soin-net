package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.Request_EstimateDao;
import com.soin.sgrm.dao.pos.PRequest_EstimateDao;
import com.soin.sgrm.model.pos.PRequest_Estimate;

@Service("PRequest_EstimateService")
@Transactional("transactionManagerPos")
public class PRequest_EstimateServiceImpl implements PRequest_EstimateService{

	@Autowired
	PRequest_EstimateDao dao;
	
	@Override
	public PRequest_Estimate findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PRequest_Estimate findByKey(String name, String value) {

		return dao.getByKey(name, value);
	}

	@Override
	public List<PRequest_Estimate> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PRequest_Estimate model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PRequest_Estimate model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PRequest_Estimate model) {
		dao.update(model);
	}

	@Override
	public PRequest_Estimate findByIdRequest(Long idRequest) {
		// TODO Auto-generated method stub
		return dao.findByIdRequest(idRequest);
	}


}

package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.model.pos.PRequest;

@Service("requestService")
@Transactional("transactionManagerPos")
public class RequestServiceImpl implements RequestService {

	@Override
	public PRequest findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PRequest findByKey(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PRequest> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(PRequest model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PRequest model) {
		// TODO Auto-generated method stub
		
	}

}

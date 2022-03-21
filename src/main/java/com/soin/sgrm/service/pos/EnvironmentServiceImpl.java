package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.model.pos.PEnvironment;

@Service("environmentService")
@Transactional("transactionManagerPos")
public class EnvironmentServiceImpl implements EnvironmentService {

	@Override
	public PEnvironment findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PEnvironment findByKey(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PEnvironment> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(PEnvironment model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PEnvironment model) {
		// TODO Auto-generated method stub
		
	}

}

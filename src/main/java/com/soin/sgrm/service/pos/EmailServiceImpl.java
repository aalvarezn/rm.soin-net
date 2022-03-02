package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.model.pos.PEmail;

@Service("emailService")
@Transactional("transactionManagerPos")
public class EmailServiceImpl implements EmailService {

	@Override
	public PEmail findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PEmail findByKey(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PEmail> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(PEmail model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PEmail model) {
		// TODO Auto-generated method stub
		
	}

}

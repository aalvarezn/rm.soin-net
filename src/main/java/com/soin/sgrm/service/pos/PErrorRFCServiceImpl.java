package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ErrorRFCDao;
import com.soin.sgrm.dao.pos.PErrorRFCDao;
import com.soin.sgrm.model.Errors_RFC;
import com.soin.sgrm.model.pos.PErrors_RFC;

@Transactional("transactionManagerPos")
@Service("PErrorRFCService")
public class PErrorRFCServiceImpl  implements PErrorRFCService{
	@Autowired
	PErrorRFCDao dao;
	
	@Override
	public PErrors_RFC findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public PErrors_RFC findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<PErrors_RFC> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(PErrors_RFC model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PErrors_RFC model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PErrors_RFC model) {
		dao.update(model);
	}

}
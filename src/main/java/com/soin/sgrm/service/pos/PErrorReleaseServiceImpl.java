package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PErrorReleaseDao;
import com.soin.sgrm.model.pos.PErrors_Release;

@Transactional("transactionManagerPos")
@Service("PErrorReleaseService")
public class PErrorReleaseServiceImpl  implements PErrorReleaseService{
	@Autowired
	PErrorReleaseDao dao;
	
	@Override
	public PErrors_Release findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public PErrors_Release findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<PErrors_Release> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(PErrors_Release model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PErrors_Release model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PErrors_Release model) {
		dao.update(model);
	}

}
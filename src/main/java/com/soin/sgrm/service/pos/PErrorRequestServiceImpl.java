package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ErrorRequestDao;
import com.soin.sgrm.dao.pos.PErrorRequestDao;
import com.soin.sgrm.model.Errors_Requests;
import com.soin.sgrm.model.pos.PErrors_Requests;


@Transactional("transactionManagerPos")
@Service("PErrorRequestService")
public class PErrorRequestServiceImpl  implements PErrorRequestService{
	@Autowired
	PErrorRequestDao dao;
	
	@Override
	public PErrors_Requests findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public PErrors_Requests findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<PErrors_Requests> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(PErrors_Requests model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PErrors_Requests model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PErrors_Requests model) {
		dao.update(model);
	}

}
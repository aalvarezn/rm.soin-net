package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.StatusIncidenceDao;
import com.soin.sgrm.dao.StatusRequestDao;
import com.soin.sgrm.dao.pos.PStatusIncidenceDao;
import com.soin.sgrm.model.StatusIncidence;
import com.soin.sgrm.model.StatusRequest;
import com.soin.sgrm.model.pos.PStatusIncidence;

@Transactional("transactionManagerPos")
@Service("PStatusIncidenceService")
public class PStatusIncidenceServiceImpl implements PStatusIncidenceService{

	@Autowired
	PStatusIncidenceDao dao;
	
	@Override
	public PStatusIncidence findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public PStatusIncidence findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<PStatusIncidence> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(PStatusIncidence model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PStatusIncidence model=findById(id);
		dao.delete(model);
		
	}

	@Override
	public void update(PStatusIncidence model) {
		dao.update(model);
	}

}

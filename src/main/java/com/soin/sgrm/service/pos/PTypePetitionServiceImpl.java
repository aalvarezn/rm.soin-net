package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PTypePetitionDao;
import com.soin.sgrm.model.pos.PTypePetition;


@Transactional("transactionManagerPos")
@Service("PTypePetitionService")
public class PTypePetitionServiceImpl implements PTypePetitionService  {
	
	@Autowired 
	PTypePetitionDao dao;
	
	@Override
	public PTypePetition findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public PTypePetition findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<PTypePetition> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(PTypePetition model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PTypePetition model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PTypePetition model) {
		dao.update(model);
	}
	@Override
	public List<PTypePetition> listTypePetition(){
		return dao.listTypePetition();
	
	}



}

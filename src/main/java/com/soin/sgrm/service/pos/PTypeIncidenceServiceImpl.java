package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.TypeIncidenceDao;
import com.soin.sgrm.dao.TypePetitionDao;
import com.soin.sgrm.dao.pos.PTypeIncidenceDao;
import com.soin.sgrm.model.TypeIncidence;
import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.model.pos.PTypeIncidence;


@Transactional("transactionManagerPos")
@Service("PTypeIncidenceService")
public class PTypeIncidenceServiceImpl implements PTypeIncidenceService  {
	
	@Autowired 
	PTypeIncidenceDao dao;
	
	@Override
	public PTypeIncidence findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public PTypeIncidence findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<PTypeIncidence> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(PTypeIncidence model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PTypeIncidence model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PTypeIncidence model) {
		dao.update(model);
	}
	@Override
	public List<PTypeIncidence> listTypePetition(){
		return dao.listTypePetition();
	
	}

}

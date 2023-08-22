package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PEmailIncidenceDao;
import com.soin.sgrm.model.pos.PEmailIncidence;


@Transactional("transactionManagerPos")
@Service("PEmailIncidenceService")
public class PEmailIncidenceServiceImpl implements PEmailIncidenceService  {
	
	@Autowired 
	PEmailIncidenceDao dao;
	
	@Override
	public PEmailIncidence findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public PEmailIncidence findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<PEmailIncidence> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(PEmailIncidence model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PEmailIncidence model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PEmailIncidence model) {
		dao.update(model);
	}
	@Override
	public List<PEmailIncidence> listTypePetition(){
		return dao.listTypePetition();
	
	}

	@Override
	public List<PEmailIncidence> findBySystem(Integer id) {
		return dao.findBySystem(id);
	}

	@Override
	public PEmailIncidence findByIdAndSys(Integer systemId, Long priorityId) {
		return dao.findByIdAndSys(systemId,priorityId);
	}

}

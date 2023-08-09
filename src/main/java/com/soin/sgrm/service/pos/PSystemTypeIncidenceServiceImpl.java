package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PSystemTypeIncidenceDao;
import com.soin.sgrm.model.pos.PSystemTypeIncidence;


@Transactional("transactionManagerPos")
@Service("PSystemTypeIncidenceService")
public class PSystemTypeIncidenceServiceImpl implements PSystemTypeIncidenceService  {
	
	@Autowired 
	PSystemTypeIncidenceDao dao;
	
	@Override
	public PSystemTypeIncidence findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public PSystemTypeIncidence findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<PSystemTypeIncidence> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(PSystemTypeIncidence model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PSystemTypeIncidence model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PSystemTypeIncidence model) {
		dao.update(model);
	}
	@Override
	public List<PSystemTypeIncidence> listTypePetition(){
		return dao.listTypePetition();
	
	}

	@Override
	public List<PSystemTypeIncidence> findBySystem(Integer id) {
		return dao.findBySystem(id);
	}

	@Override
	public PSystemTypeIncidence findByIdAndSys(Integer systemId, Long priorityId) {
		return dao.findByIdAndSys(systemId,priorityId);
	}

	@Override
	public List<PSystemTypeIncidence> findByManager(Integer idUser) {
	
		 return dao.findByManager(idUser);
	}

}
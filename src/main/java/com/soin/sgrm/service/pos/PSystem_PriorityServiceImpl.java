package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PSystem_PriorityDao;
import com.soin.sgrm.model.pos.PSystem_Priority;


@Transactional("transactionManagerPos")
@Service("PSystem_PriorityService")
public class PSystem_PriorityServiceImpl implements PSystem_PriorityService  {
	
	@Autowired 
	PSystem_PriorityDao dao;
	
	@Override
	public PSystem_Priority findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public PSystem_Priority findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<PSystem_Priority> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(PSystem_Priority model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PSystem_Priority model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PSystem_Priority model) {
		dao.update(model);
	}
	@Override
	public List<PSystem_Priority> listTypePetition(){
		return dao.listTypePetition();
	
	}

	@Override
	public List<PSystem_Priority> findBySystem(Integer id) {
		return dao.findBySystem(id);
	}

	@Override
	public PSystem_Priority findByIdAndSys(Integer systemId, Long priorityId) {
		return dao.findByIdAndSys(systemId,priorityId);
	}
	@Override
	public List<PSystem_Priority> findByManger(Integer idUser){
		return dao.findByManger(idUser);
	}
}
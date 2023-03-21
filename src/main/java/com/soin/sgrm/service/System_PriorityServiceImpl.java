package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.System_PriorityDao;
import com.soin.sgrm.dao.TypeIncidenceDao;
import com.soin.sgrm.dao.TypePetitionDao;
import com.soin.sgrm.model.System_Priority;
import com.soin.sgrm.model.TypeIncidence;
import com.soin.sgrm.model.TypePetition;


@Transactional("transactionManager")
@Service("System_PriorityService")
public class System_PriorityServiceImpl implements System_PriorityService  {
	
	@Autowired 
	System_PriorityDao dao;
	
	@Override
	public System_Priority findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public System_Priority findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<System_Priority> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(System_Priority model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		System_Priority model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(System_Priority model) {
		dao.update(model);
	}
	@Override
	public List<System_Priority> listTypePetition(){
		return dao.listTypePetition();
	
	}

	@Override
	public List<System_Priority> findBySystem(Integer id) {
		return dao.findBySystem(id);
	}

	@Override
	public System_Priority findByIdAndSys(Integer systemId, Long priorityId) {
		return dao.findByIdAndSys(systemId,priorityId);
	}
	@Override
	public List<System_Priority> findByManger(Integer idUser){
		return dao.findByManger(idUser);
	}
}
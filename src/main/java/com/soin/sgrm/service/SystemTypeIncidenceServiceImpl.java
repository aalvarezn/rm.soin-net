package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.SystemTypeIncidenceDao;
import com.soin.sgrm.dao.System_PriorityDao;
import com.soin.sgrm.dao.TypeIncidenceDao;
import com.soin.sgrm.dao.TypePetitionDao;
import com.soin.sgrm.model.SystemTypeIncidence;
import com.soin.sgrm.model.System_Priority;
import com.soin.sgrm.model.TypeIncidence;
import com.soin.sgrm.model.TypePetition;


@Transactional("transactionManager")
@Service("SystemTypeIncidenceService")
public class SystemTypeIncidenceServiceImpl implements SystemTypeIncidenceService  {
	
	@Autowired 
	SystemTypeIncidenceDao dao;
	
	@Override
	public SystemTypeIncidence findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public SystemTypeIncidence findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<SystemTypeIncidence> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(SystemTypeIncidence model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		SystemTypeIncidence model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(SystemTypeIncidence model) {
		dao.update(model);
	}
	@Override
	public List<SystemTypeIncidence> listTypePetition(){
		return dao.listTypePetition();
	
	}

	@Override
	public List<SystemTypeIncidence> findBySystem(Integer id) {
		return dao.findBySystem(id);
	}

	@Override
	public SystemTypeIncidence findByIdAndSys(Integer systemId, Long priorityId) {
		return dao.findByIdAndSys(systemId,priorityId);
	}

}

package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.System_StatusInDao;
import com.soin.sgrm.model.System_StatusIn;


@Transactional("transactionManager")
@Service("System_StatusInService")
public class System_StatusInServiceImpl implements System_StatusInService  {
	
	@Autowired 
	System_StatusInDao dao;
	
	@Override
	public System_StatusIn findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public System_StatusIn findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<System_StatusIn> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(System_StatusIn model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		System_StatusIn model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(System_StatusIn model) {
		dao.update(model);
	}
	@Override
	public List<System_StatusIn> listTypePetition(){
		return dao.listTypePetition();
	
	}

	@Override
	public List<System_StatusIn> findBySystem(Integer id) {
		return dao.findBySystem(id);
	}

	@Override
	public System_StatusIn findByIdAndSys(Integer systemId, Long priorityId) {
		return dao.findByIdAndSys(systemId,priorityId);
	}

	@Override
	public List<System_StatusIn> findByManger(Integer idUser) {
		return dao.findByManger(idUser);
	}

	

	@Override
	public System_StatusIn findByIdByCode(int id, String code) {
		return dao.findByIdByCode(id,code);
	}

	@Override
	public System_StatusIn findByIdByName(int id, String name) {
		return dao.findByIdByName(id,name);
	}

}

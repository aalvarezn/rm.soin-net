package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PSystem_StatusInDao;
import com.soin.sgrm.model.pos.PSystem_StatusIn;



@Transactional("transactionManagerPos")
@Service("PSystem_StatusInService")
public class PSystem_StatusInServiceImpl implements PSystem_StatusInService  {
	
	@Autowired 
	PSystem_StatusInDao dao;
	
	@Override
	public PSystem_StatusIn findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public PSystem_StatusIn findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<PSystem_StatusIn> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(PSystem_StatusIn model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PSystem_StatusIn model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PSystem_StatusIn model) {
		dao.update(model);
	}
	@Override
	public List<PSystem_StatusIn> listTypePetition(){
		return dao.listTypePetition();
	
	}

	@Override
	public List<PSystem_StatusIn> findBySystem(Integer id) {
		return dao.findBySystem(id);
	}

	@Override
	public PSystem_StatusIn findByIdAndSys(Integer systemId, Long priorityId) {
		return dao.findByIdAndSys(systemId,priorityId);
	}

	@Override
	public List<PSystem_StatusIn> findByManger(Integer idUser) {
		return dao.findByManger(idUser);
	}

	

	@Override
	public PSystem_StatusIn findByIdByCode(int id, String code) {
		return dao.findByIdByCode(id,code);
	}

	@Override
	public PSystem_StatusIn findByIdByName(int id, String name) {
		return dao.findByIdByName(id,name);
	}

}

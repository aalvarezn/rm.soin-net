package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.TypeIncidenceDao;
import com.soin.sgrm.dao.TypePetitionDao;
import com.soin.sgrm.model.TypeIncidence;
import com.soin.sgrm.model.TypePetition;


@Transactional("transactionManager")
@Service("TypeIncidenceService")
public class TypeIncidenceServiceImpl implements TypeIncidenceService  {
	
	@Autowired 
	TypeIncidenceDao dao;
	
	@Override
	public TypeIncidence findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public TypeIncidence findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<TypeIncidence> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(TypeIncidence model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		TypeIncidence model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(TypeIncidence model) {
		dao.update(model);
	}
	@Override
	public List<TypeIncidence> listTypePetition(){
		return dao.listTypePetition();
	
	}

}

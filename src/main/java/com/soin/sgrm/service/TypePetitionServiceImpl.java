package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.TypePetitionDao;
import com.soin.sgrm.model.TypePetition;


@Transactional("transactionManager")
@Service("TypePetitionService")
public class TypePetitionServiceImpl implements TypePetitionService  {
	
	@Autowired 
	TypePetitionDao dao;
	
	@Override
	public TypePetition findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public TypePetition findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<TypePetition> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(TypePetition model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		TypePetition model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(TypePetition model) {
		dao.update(model);
	}
	@Override
	public List<TypePetition> listTypePetition(){
		return dao.listTypePetition();
	
	}

}

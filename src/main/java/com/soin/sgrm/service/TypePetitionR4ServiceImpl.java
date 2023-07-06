package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.TypePetitionDao;
import com.soin.sgrm.dao.TypePetitionR4Dao;
import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.model.TypePetitionR4;


@Transactional("transactionManager")
@Service("TypePetitionR4Service")
public class TypePetitionR4ServiceImpl implements TypePetitionR4Service  {
	
	@Autowired 
	TypePetitionR4Dao dao;
	
	@Override
	public TypePetitionR4 findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public TypePetitionR4 findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<TypePetitionR4> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(TypePetitionR4 model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		TypePetitionR4 model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(TypePetitionR4 model) {
		dao.update(model);
	}
	@Override
	public List<TypePetitionR4> listTypePetition(){
		return dao.listTypePetition();
	
	}

}

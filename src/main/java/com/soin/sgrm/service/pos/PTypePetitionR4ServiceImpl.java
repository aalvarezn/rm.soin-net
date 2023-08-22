package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.TypePetitionDao;
import com.soin.sgrm.dao.TypePetitionR4Dao;
import com.soin.sgrm.dao.pos.PTypePetitionR4Dao;
import com.soin.sgrm.model.TypePetition;
import com.soin.sgrm.model.TypePetitionR4;
import com.soin.sgrm.model.pos.PTypePetitionR4;


@Transactional("transactionManagerPos")
@Service("PTypePetitionR4Service")
public class PTypePetitionR4ServiceImpl implements PTypePetitionR4Service  {
	
	@Autowired 
	PTypePetitionR4Dao dao;
	
	@Override
	public PTypePetitionR4 findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public PTypePetitionR4 findByKey(String name, String value) {
		
		return dao.getByKey(name, value);
	}

	@Override
	public List<PTypePetitionR4> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(PTypePetitionR4 model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PTypePetitionR4 model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PTypePetitionR4 model) {
		dao.update(model);
	}
	@Override
	public List<PTypePetitionR4> listTypePetition(){
		return dao.listTypePetition();
	
	}

}

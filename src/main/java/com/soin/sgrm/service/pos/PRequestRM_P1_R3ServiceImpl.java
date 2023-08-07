package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PRequestRM_P1_R3Dao;
import com.soin.sgrm.model.pos.PRequestRM_P1_R3;

@Service("PRequestRM_P1_R3Service")
@Transactional("transactionManagerPos")
public class PRequestRM_P1_R3ServiceImpl implements PRequestRM_P1_R3Service{
	@Autowired
	PRequestRM_P1_R3Dao dao; 

	@Override
	public PRequestRM_P1_R3 findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PRequestRM_P1_R3 findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public List<PRequestRM_P1_R3> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PRequestRM_P1_R3 model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PRequestRM_P1_R3 model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PRequestRM_P1_R3 model) {
		dao.update(model);
	}
	
	@Override
	public PRequestRM_P1_R3 requestRm3(Long id){
		return dao.requestRm3(id);
	}

}

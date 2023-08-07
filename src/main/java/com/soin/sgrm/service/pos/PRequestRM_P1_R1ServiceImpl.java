package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PRequestRM_P1_R1Dao;
import com.soin.sgrm.model.pos.PRequestRM_P1_R1;

@Service("PRequestRM_P1_R1Service")
@Transactional("transactionManagerPos")
public class PRequestRM_P1_R1ServiceImpl implements PRequestRM_P1_R1Service{
	@Autowired
	PRequestRM_P1_R1Dao dao; 

	@Override
	public PRequestRM_P1_R1 findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PRequestRM_P1_R1 findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public List<PRequestRM_P1_R1> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PRequestRM_P1_R1 model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PRequestRM_P1_R1 model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PRequestRM_P1_R1 model) {
		dao.update(model);
	}
	
	@Override
	public PRequestRM_P1_R1 requestRm1(Long id){
		return dao.requestRM1(id);
	}



}

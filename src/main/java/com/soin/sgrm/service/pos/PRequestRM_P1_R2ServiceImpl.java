package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PRequestRM_P1_R2Dao;
import com.soin.sgrm.model.pos.PRequestRM_P1_R2;

@Service("PRequestRM_P1_R2Service")
@Transactional("transactionManagerPos")
public class PRequestRM_P1_R2ServiceImpl implements PRequestRM_P1_R2Service{
	@Autowired
	PRequestRM_P1_R2Dao dao; 

	@Override
	public PRequestRM_P1_R2 findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PRequestRM_P1_R2 findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public List<PRequestRM_P1_R2> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PRequestRM_P1_R2 model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PRequestRM_P1_R2 model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PRequestRM_P1_R2 model) {
		dao.update(model);
	}
	
	@Override
	public PRequestRM_P1_R2 requestRm2(Long id){
		return dao.requestRM2(id);
	}

}

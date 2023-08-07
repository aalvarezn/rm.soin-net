package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PRequestRM_P1_R5Dao;
import com.soin.sgrm.model.pos.PRequestRM_P1_R5;



@Service("PRequestRM_P1_R5Service")
@Transactional("transactionManagerPos")
public class PRequestRM_P1_R5ServiceImpl implements PRequestRM_P1_R5Service{
	@Autowired
	PRequestRM_P1_R5Dao dao; 

	@Override
	public PRequestRM_P1_R5 findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PRequestRM_P1_R5 findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public List<PRequestRM_P1_R5> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PRequestRM_P1_R5 model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PRequestRM_P1_R5 model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PRequestRM_P1_R5 model) {
		dao.update(model);
	}
	
	@Override
	public PRequestRM_P1_R5 requestRm5(Long id){
		return dao.requestRM5(id);
	}

}

package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PRequestRM_P1_R4Dao;
import com.soin.sgrm.model.pos.PRequestRM_P1_R4;


@Service("PRequestRM_P1_R4Service")
@Transactional("transactionManagerPos")
public class PRequestRM_P1_R4ServiceImpl implements PRequestRM_P1_R4Service{
	@Autowired
	PRequestRM_P1_R4Dao dao; 

	@Override
	public PRequestRM_P1_R4 findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PRequestRM_P1_R4 findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public List<PRequestRM_P1_R4> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PRequestRM_P1_R4 model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PRequestRM_P1_R4 model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PRequestRM_P1_R4 model) {
		dao.update(model);
	}
	
	@Override
	public List<PRequestRM_P1_R4> listRequestRm4(Long id){
		return dao.listRequestRm4(id);
	}

}

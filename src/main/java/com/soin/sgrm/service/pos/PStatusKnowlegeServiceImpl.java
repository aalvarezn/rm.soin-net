package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.StatusKnowlegeDao;
import com.soin.sgrm.dao.pos.PStatusKnowlegeDao;
import com.soin.sgrm.model.StatusKnowlege;
import com.soin.sgrm.model.pos.PStatusKnowlege;

@Transactional("transactionManagerPos")
@Service("PStatusKnowlegeService")
public class PStatusKnowlegeServiceImpl  implements PStatusKnowlegeService{
	@Autowired
	PStatusKnowlegeDao dao;
	
	@Override
	public PStatusKnowlege findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public PStatusKnowlege findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<PStatusKnowlege> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(PStatusKnowlege model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PStatusKnowlege model=findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PStatusKnowlege model) {
		dao.update(model);
	}

}

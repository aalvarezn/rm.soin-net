package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ImpactDao;
import com.soin.sgrm.dao.pos.PImpactDao;
import com.soin.sgrm.model.Impact;
import com.soin.sgrm.model.pos.PImpact;

@Transactional("transactionManagerPos")
@Service("PImpactService")
public class PImpactServiceImpl implements PImpactService {

	@Autowired
	PImpactDao dao;

	@Override
	public List<PImpact> list() {
		return dao.list();
	}

	@Override
	public PImpact findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void save(PImpact impact) {
		dao.save(impact);
	}

	@Override
	public void update(PImpact impact) {
		dao.update(impact);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

}

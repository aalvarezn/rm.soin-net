package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ImpactDao;
import com.soin.sgrm.model.Impact;

@Transactional("transactionManager")
@Service("ImpactService")
public class ImpactServiceImpl implements ImpactService {

	@Autowired
	ImpactDao dao;

	@Override
	public List<Impact> list() {
		return dao.list();
	}

	@Override
	public Impact findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void save(Impact impact) {
		dao.save(impact);
	}

	@Override
	public void update(Impact impact) {
		dao.update(impact);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

}

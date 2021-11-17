package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.RiskDao;
import com.soin.sgrm.model.Risk;

@Transactional("transactionManager")
@Service("RiskService")
public class RiskServiceImpl implements RiskService {

	@Autowired
	RiskDao dao;

	@Override
	public List<Risk> list() {
		return dao.list();
	}

	@Override
	public Risk findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void save(Risk risk) {
		dao.save(risk);
	}

	@Override
	public void update(Risk risk) {
		dao.update(risk);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

}
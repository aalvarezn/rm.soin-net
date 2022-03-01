package com.soin.sgrm.service.pos;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.RiskDao;
import com.soin.sgrm.model.pos.PRisk;

@Service("riskService")
@Transactional("transactionManagerPos")
public class RiskServiceImpl implements RiskService {

	@Autowired
	RiskDao dao;

	@Override
	public PRisk findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PRisk findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public List<PRisk> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PRisk model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PRisk model = findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PRisk model) {
		dao.update(model);
	}

}

package com.soin.sgrm.service.pos;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.ImpactDao;
import com.soin.sgrm.model.pos.PImpact;

@Service("impactService")
@Transactional("transactionManagerPos")
public class ImpactServiceImpl implements ImpactService {

	@Autowired
	ImpactDao dao;

	@Override
	public PImpact findById(Long id) {
		return dao.getById(id);

	}

	@Override
	public PImpact findByKey(String name, String value) {
		return dao.getByKey(name, value);

	}

	@Override
	public List<PImpact> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PImpact model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PImpact model = findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PImpact model) {
		dao.update(model);

	}

}

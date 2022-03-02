package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.ParameterDao;
import com.soin.sgrm.model.pos.PParameter;


@Service("parameterService")
@Transactional("transactionManagerPos")
public class ParameterServiceImpl implements ParameterService {
	
	@Autowired
	ParameterDao dao;
	
	@Override
	public PParameter findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PParameter findByKey(String name, String value) {
		return dao.getByKey(name, value);

	}

	@Override
	public List<PParameter> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PParameter model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PParameter model = findById(id);
		dao.delete(model);
		
	}

	@Override
	public void update(PParameter model) {
		dao.update(model);
	}

}

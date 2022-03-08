package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.AmbientDao;
import com.soin.sgrm.model.pos.PAmbient;

@Service("ambientService")
@Transactional("transactionManagerPos")
public class AmbientServiceImpl implements AmbientService{

	@Autowired
	AmbientDao dao;
	
	@Override
	public PAmbient findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PAmbient findByKey(String name, String value) {
	
		return dao.getByKey(name, value);
	}

	@Override
	public List<PAmbient> findAll() {
		// TODO Auto-generated method stub
		return dao.findAll();
	}

	@Override
	public void save(PAmbient model) {
		dao.save(model);
		
	}

	@Override
	public void delete(Long id) {
		PAmbient model= findById(id);
		dao.delete(model);
		
	}

	@Override
	public void update(PAmbient model) {
		dao.update(model);
	}

}

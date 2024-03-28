package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.TypeAmbientDao;
import com.soin.sgrm.dao.pos.PTypeAmbientDao;
import com.soin.sgrm.model.TypeAmbient;
import com.soin.sgrm.model.pos.PTypeAmbient;

@Transactional("transactionManagerPos")
@Service("PTypeAmbientService")
public class PTypeAmbientServiceImpl implements PTypeAmbientService {

	@Autowired
	PTypeAmbientDao dao;

	@Override
	public List<PTypeAmbient> list() {
		return dao.list();
	}

	@Override
	public PTypeAmbient findByName(String name) {
		return dao.findByName(name);
	}

	@Override
	public PTypeAmbient findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void save(PTypeAmbient typeambient) {
		dao.save(typeambient);
	}

	@Override
	public void update(PTypeAmbient typeambient) {
		dao.update(typeambient);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

}

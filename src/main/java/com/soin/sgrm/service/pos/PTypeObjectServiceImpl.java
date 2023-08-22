package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.TypeObjectDao;
import com.soin.sgrm.dao.pos.PTypeObjectDao;
import com.soin.sgrm.model.TypeObject;
import com.soin.sgrm.model.pos.PTypeObject;

@Transactional("transactionManagerPos")
@Service("PTypeObjectService")
public class PTypeObjectServiceImpl implements PTypeObjectService {

	@Autowired
	PTypeObjectDao dao;

	@Override
	public List<PTypeObject> listBySystem(Integer id) {
		return dao.listBySystem(id);
	}

	@Override
	public boolean existTypeObject(String name, Integer system_id) {
		return dao.existTypeObject(name, system_id);
	}

	@Override
	public PTypeObject findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public PTypeObject findByName(String name, String extension, Integer system_id) {
		return dao.findByName(name, extension, system_id);
	}

	@Override
	public PTypeObject findByName(String name, Integer system_id) {
		return dao.findByName(name, "", system_id);
	}

	@Override
	public PTypeObject save(PTypeObject type) {
		return dao.save(type);
	}

	@Override
	public List<PTypeObject> list() {
		return dao.list();
	}

	@Override
	public void update(PTypeObject typeObject) {
		dao.update(typeObject);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

}

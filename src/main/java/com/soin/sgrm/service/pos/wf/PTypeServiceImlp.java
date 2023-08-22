package com.soin.sgrm.service.pos.wf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.wf.PTypeDao;
import com.soin.sgrm.model.pos.wf.PType;

@Transactional("transactionManagerPos")
@Service("PTypeService")
public class PTypeServiceImlp implements PTypeService {

	@Autowired
	PTypeDao dao;

	@Override
	public List<PType> list() {
		return dao.list();
	}

	@Override
	public PType findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void save(PType type) {
		dao.save(type);
	}

	@Override
	public void update(PType type) {
		dao.update(type);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

}

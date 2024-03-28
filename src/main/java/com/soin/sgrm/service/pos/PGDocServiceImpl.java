package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PGDocDao;
import com.soin.sgrm.model.pos.PGDoc;

@Transactional("transactionManagerPos")
@Service("PGDocService")
public class PGDocServiceImpl implements PGDocService {

	@Autowired
	PGDocDao dao;

	@Override
	public List<PGDoc> list() {
		return dao.list();
	}

	@Override
	public PGDoc findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void save(PGDoc gDoc) {
		dao.save(gDoc);
	}

	@Override
	public void update(PGDoc gDoc) {
		dao.update(gDoc);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

	
}

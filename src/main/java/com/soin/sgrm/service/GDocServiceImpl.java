package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.GDocDao;
import com.soin.sgrm.dao.ProjectDao;
import com.soin.sgrm.model.GDoc;
import com.soin.sgrm.model.Project;

@Transactional("transactionManager")
@Service("GDocService")
public class GDocServiceImpl implements GDocService {

	@Autowired
	GDocDao dao;

	@Override
	public List<GDoc> list() {
		return dao.list();
	}

	@Override
	public GDoc findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void save(GDoc gDoc) {
		dao.save(gDoc);
	}

	@Override
	public void update(GDoc gDoc) {
		dao.update(gDoc);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

	
}

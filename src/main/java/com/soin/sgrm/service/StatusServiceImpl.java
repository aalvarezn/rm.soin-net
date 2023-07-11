package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.StatusDao;
import com.soin.sgrm.model.Status;

@Transactional("transactionManager")
@Service("ReleaseStatusService")
public class StatusServiceImpl implements StatusService {

	@Autowired
	StatusDao dao;

	@Override
	public List<Status> list() {
		return dao.list();
	}

	@Override
	public Status findByName(String name) {
		return dao.findByName(name);
	}

	@Override
	public Status findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void save(Status status) {
		dao.save(status);
	}

	@Override
	public void update(Status status) {
		dao.update(status);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

	@Override
	public List<Status> listWithOutAnul() {
		// TODO Auto-generated method stub
		return dao.listWithOutAnul();
	}

}

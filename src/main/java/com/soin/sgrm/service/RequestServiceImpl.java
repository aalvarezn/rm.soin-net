package com.soin.sgrm.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.RequestDao;
import com.soin.sgrm.model.Request;
import com.soin.sgrm.model.TypeRequest;


@Transactional("transactionManager")
@Service("RequestService")
public class RequestServiceImpl implements RequestService {

	@Autowired
	RequestDao dao;

	@Override
	public List<Request> list(String search, Object[] projects) throws SQLException {
		return dao.list(search, projects);
	}

	@Override
	public Request findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public Request findByName(String code_soin, String code_ice) {
		return dao.findByName(code_soin, code_ice);
	}

	@Override
	public Request findByName(String code_soin) {
		return dao.findByName(code_soin);
	}

	@Override
	public List<Request> list() {
		return dao.list();
	}

	@Override
	public void save(Request request) {
		dao.save(request);
	}

	@Override
	public void update(Request request) {
		dao.update(request);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

	@Override
	public List<Request> listByType(TypeRequest type) {
		return dao.listByType(type);
	}

	@Override
	public void softDelete(Request request) {
		dao.softDelete(request);
	}

}

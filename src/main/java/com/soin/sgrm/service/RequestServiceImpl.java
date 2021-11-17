package com.soin.sgrm.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.RequestDao;
import com.soin.sgrm.model.Request;

@Transactional("transactionManager")
@Service("RequestService")
public class RequestServiceImpl implements RequestService {

	@Autowired
	RequestDao dao;

	@Override
	public List<Request> list(String search) throws SQLException {
		return dao.list(search);
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

}

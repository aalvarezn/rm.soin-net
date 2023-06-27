package com.soin.sgrm.service.pos;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.RequestDao;
import com.soin.sgrm.dao.pos.PRequestDao;
import com.soin.sgrm.model.Request;
import com.soin.sgrm.model.TypeRequest;
import com.soin.sgrm.model.pos.PRequest;
import com.soin.sgrm.model.pos.PTypeRequest;
import com.soin.sgrm.utils.JsonSheet;

@Transactional("transactionManagerPos")
@Service("PRequestService")
public class PRequestServiceImpl implements PRequestService {

	@Autowired
	PRequestDao dao;

	@Override
	public List<PRequest> list(String search, Object[] projects) throws SQLException {
		return dao.list(search, projects);
	}

	@Override
	public PRequest findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public PRequest findByName(String code_soin, String code_ice) {
		return dao.findByName(code_soin, code_ice);
	}

	@Override
	public PRequest findByName(String code_soin) {
		return dao.findByName(code_soin);
	}

	@Override
	public List<PRequest> list() {
		return dao.list();
	}

	@Override
	public void save(PRequest request) {
		dao.save(request);
	}

	@Override
	public void update(PRequest request) {
		dao.update(request);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

	@Override
	public List<PRequest> listByType(PTypeRequest type) {
		return dao.listByType(type);
	}

	@Override
	public void softDelete(PRequest request) {
		dao.softDelete(request);
	}

	@Override
	public PRequest listByTypeAndCodeSoin(PTypeRequest type, String code_soin) {
		return dao.listByTypeAndCodeSoin(type, code_soin);
	}

	@Override
	public PRequest findByNameCode(String tpo) {
		return dao.findByNameCode(tpo);
	}



}

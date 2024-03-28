package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.TypeRequestDao;
import com.soin.sgrm.dao.pos.PTypeRequestDao;
import com.soin.sgrm.model.TypeRequest;
import com.soin.sgrm.model.pos.PTypeRequest;

@Transactional("transactionManagerPos")
@Service("PTypeRequestService")
public class PTypeRequestServiceImpl implements PTypeRequestService {

	@Autowired
	PTypeRequestDao dao;

	@Override
	public List<PTypeRequest> list() {
		return dao.list();
	}

	@Override
	public PTypeRequest findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public PTypeRequest save(PTypeRequest typeRequest) {
		return dao.save(typeRequest);
	}

	@Override
	public void update(PTypeRequest typeRequest) {
		dao.update(typeRequest);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

}

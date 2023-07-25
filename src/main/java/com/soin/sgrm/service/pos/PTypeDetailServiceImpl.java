package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PTypeDetailDao;
import com.soin.sgrm.model.pos.PTypeDetail;

@Transactional("transactionManagerPos")
@Service("PTypeDetailService")
public class PTypeDetailServiceImpl implements PTypeDetailService {

	@Autowired
	PTypeDetailDao dao;

	@Override
	public List<PTypeDetail> list() {
		return dao.list();
	}

	@Override
	public PTypeDetail findByName(String name) {
		return dao.findByName(name);
	}

}

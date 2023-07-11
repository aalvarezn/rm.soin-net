package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PParameterDao;
import com.soin.sgrm.model.pos.PParameter;

@Transactional("transactionManagerPos")
@Service("PParameterService")
public class PParameterServiceImpl implements PParameterService {

	@Autowired
	PParameterDao dao;

	@Override
	public List<PParameter> listAll() {
		return dao.listAll();
	}

	@Override
	public PParameter findByCode(Integer code) {
		return dao.findByCode(code);
	}

	@Override
	public PParameter findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void updateParameter(PParameter param) {
		dao.updatePParameter(param);
	}

	@Override
	public PParameter getParameterByCode(Integer code) {
		return dao.getPParameterByCode(code);
	}

}

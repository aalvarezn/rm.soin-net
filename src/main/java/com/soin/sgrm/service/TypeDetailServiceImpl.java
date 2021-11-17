package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.TypeDetailDao;
import com.soin.sgrm.model.TypeDetail;

@Transactional("transactionManager")
@Service("TypeDetailService")
public class TypeDetailServiceImpl implements TypeDetailService {

	@Autowired
	TypeDetailDao dao;

	@Override
	public List<TypeDetail> list() {
		return dao.list();
	}

	@Override
	public TypeDetail findByName(String name) {
		return dao.findByName(name);
	}

}

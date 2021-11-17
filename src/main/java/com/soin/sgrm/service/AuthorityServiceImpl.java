package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.AuthorityDao;
import com.soin.sgrm.model.Authority;

@Transactional("transactionManager")
@Service("AuthorityService")
public class AuthorityServiceImpl implements AuthorityService {

	@Autowired
	AuthorityDao dao;

	@Override
	public List<Authority> list() {
		return dao.list();
	}

	@Override
	public Authority findById(Integer id) {
		return dao.findById(id);
	}

}

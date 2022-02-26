package com.soin.sgrm.service.pos;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.AuthorityDao;
import com.soin.sgrm.model.pos.PAuthority;

@Service("authorityService")
@Transactional("transactionManagerPos")
public class AuthorityServiceImpl implements AuthorityService {

	@Autowired
	AuthorityDao dao;

	@Override
	public PAuthority findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PAuthority findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public List<PAuthority> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PAuthority model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PAuthority model = findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PAuthority model) {
		dao.update(model);
	}

}

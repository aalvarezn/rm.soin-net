package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PAuthorityDao;
import com.soin.sgrm.model.pos.PAuthority;

@Transactional("transactionManagerPos")
@Service("PAuthorityService")
public class PAuthorityServiceImpl implements PAuthorityService {

	@Autowired
	PAuthorityDao dao;

	@Override
	public List<PAuthority> list() {
		return dao.list();
	}

	@Override
	public PAuthority findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void save(PAuthority authority) {
		dao.save(authority);
	}

	@Override
	public void update(PAuthority authority) {
		dao.update(authority);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

	@Override
	public PAuthority findByName(String name) {
		
		return dao.findByName(name);
	}

}

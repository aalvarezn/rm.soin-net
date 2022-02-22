package com.soin.sgrm.service.wf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.wf.TypeDao;
import com.soin.sgrm.model.wf.Type;

@Transactional("transactionManager")
@Service("TypeService")
public class TypeServiceImlp implements TypeService {

	@Autowired
	TypeDao dao;

	@Override
	public List<Type> list() {
		return dao.list();
	}

	@Override
	public Type findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void save(Type type) {
		dao.save(type);
	}

	@Override
	public void update(Type type) {
		dao.update(type);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

}

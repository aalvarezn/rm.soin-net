package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.ReleaseDao;
import com.soin.sgrm.model.pos.PRelease;

@Service("releaseService")
@Transactional("transactionManagerPos")
public class ReleaseServiceImpl implements ReleaseService {
	
	@Autowired
	ReleaseDao dao;
	@Override
	public PRelease findById(Long id) {
		
		return dao.getById(id);
	}

	@Override
	public PRelease findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public List<PRelease> findAll() {
		
		return dao.findAll();
	}

	@Override
	public void save(PRelease model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PRelease model= findById(id);
		dao.delete(model);
	}

	@Override
	public void update(PRelease model) {
		dao.update(model);
	}

	@Override
	public List<PRelease> listReleasesBySystem(Long id) {
		return dao.listReleasesBySystem(id);
	}

}

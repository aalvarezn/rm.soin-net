package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.PriorityDao;
import com.soin.sgrm.dao.PriorityIncidenceDao;
import com.soin.sgrm.dao.pos.PPriorityIncidenceDao;
import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.PriorityIncidence;
import com.soin.sgrm.model.pos.PPriorityIncidence;

@Transactional("transactionManagerPos")
@Service("PPriorityIncidenceService")
public class PPriorityIncidenceServiceImpl implements PPriorityIncidenceService {

	@Autowired
	PPriorityIncidenceDao dao;

	@Override
	public PPriorityIncidence findById(Long id) {
		return dao.getById(id);
	}

	@Override
	public PPriorityIncidence findByKey(String name, String value) {
		return dao.getByKey(name, value);
	}

	@Override
	public List<PPriorityIncidence> findAll() {
		return dao.findAll();
	}

	@Override
	public void save(PPriorityIncidence model) {
		dao.save(model);
	}

	@Override
	public void delete(Long id) {
		PPriorityIncidence model= findById(id);
		dao.delete(model);
		
	}

	@Override
	public void update(PPriorityIncidence model) {
		dao.update(model);
	}


}


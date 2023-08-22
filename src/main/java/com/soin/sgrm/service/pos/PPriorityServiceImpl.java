package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.PriorityDao;
import com.soin.sgrm.dao.pos.PPriorityDao;
import com.soin.sgrm.model.Priority;
import com.soin.sgrm.model.pos.PPriority;

@Transactional("transactionManagerPos")
@Service("PPriorityService")
public class PPriorityServiceImpl implements PPriorityService {

	@Autowired
	PPriorityDao dao;

	@Override
	public List<PPriority> list() {
		return dao.list();
	}

	@Override
	public PPriority findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void save(PPriority priority) {
		dao.save(priority);
	}

	@Override
	public void update(PPriority priority) {
		dao.update(priority);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

}

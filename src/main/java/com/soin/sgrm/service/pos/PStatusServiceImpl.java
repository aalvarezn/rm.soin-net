package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.StatusDao;
import com.soin.sgrm.dao.pos.PStatusDao;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.pos.PStatus;

@Transactional("transactionManagerPos")
@Service("PReleaseStatusService")
public class PStatusServiceImpl implements PStatusService {

	@Autowired
	PStatusDao dao;

	@Override
	public List<PStatus> list() {
		return dao.list();
	}

	@Override
	public PStatus findByName(String name) {
		return dao.findByName(name);
	}

	@Override
	public PStatus findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void save(PStatus status) {
		dao.save(status);
	}

	@Override
	public void update(PStatus status) {
		dao.update(status);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

	@Override
	public List<PStatus> listWithOutAnul() {
		// TODO Auto-generated method stub
		return dao.listWithOutAnul();
	}

}

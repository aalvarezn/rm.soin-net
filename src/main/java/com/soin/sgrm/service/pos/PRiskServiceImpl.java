package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.RiskDao;
import com.soin.sgrm.dao.pos.PRiskDao;
import com.soin.sgrm.model.Risk;
import com.soin.sgrm.model.pos.PRisk;

@Transactional("transactionManagerPos")
@Service("PRiskService")
public class PRiskServiceImpl implements PRiskService {

	@Autowired
	PRiskDao dao;

	@Override
	public List<PRisk> list() {
		return dao.list();
	}

	@Override
	public PRisk findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void save(PRisk risk) {
		dao.save(risk);
	}

	@Override
	public void update(PRisk risk) {
		dao.update(risk);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

}
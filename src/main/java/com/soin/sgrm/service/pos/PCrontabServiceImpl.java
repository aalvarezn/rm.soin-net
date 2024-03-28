package com.soin.sgrm.service.pos;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PCrontabDao;
import com.soin.sgrm.model.pos.PButtonCommand;
import com.soin.sgrm.model.pos.PCrontab;


@Transactional("transactionManagerPos")
@Service("PCrontabService")
public class PCrontabServiceImpl implements PCrontabService {

	@Autowired
	PCrontabDao dao;

	@Override
	public PCrontab saveCrontab(PCrontab crontab) {
		return dao.saveCrontab(crontab);
	}

	@Override
	public PCrontab updateCrontab(PCrontab crontab, PButtonCommand old) {
		return dao.updateCrontab(crontab, old);
	}

	@Override
	public PCrontab findById(Integer id) throws SQLException {
		return dao.findById(id);
	}

	@Override
	public void deleteCrontab(PCrontab crontab) {
		dao.deleteCrontab(crontab);
	}

	@Override
	public PCrontab findByIdButton(Integer id) throws SQLException {
		return dao.findByIdButton(id);
	}

}

package com.soin.sgrm.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.CrontabDao;
import com.soin.sgrm.model.ButtonCommand;
import com.soin.sgrm.model.Crontab;

@Transactional("transactionManager")
@Service("CrontabService")
public class CrontabServiceImpl implements CrontabService {

	@Autowired
	CrontabDao dao;

	@Override
	public Crontab saveCrontab(Crontab crontab) {
		return dao.saveCrontab(crontab);
	}

	@Override
	public Crontab updateCrontab(Crontab crontab, ButtonCommand old) {
		return dao.updateCrontab(crontab, old);
	}

	@Override
	public Crontab findById(Integer id) throws SQLException {
		return dao.findById(id);
	}

	@Override
	public void deleteCrontab(Crontab crontab) {
		dao.deleteCrontab(crontab);
	}

	@Override
	public Crontab findByIdButton(Integer id) throws SQLException {
		return dao.findByIdButton(id);
	}

}

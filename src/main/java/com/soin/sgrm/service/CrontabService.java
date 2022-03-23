package com.soin.sgrm.service;

import java.sql.SQLException;

import com.soin.sgrm.model.ButtonCommand;
import com.soin.sgrm.model.Crontab;

public interface CrontabService {

	Crontab saveCrontab(Crontab crontab);

	Crontab updateCrontab(Crontab crontab, ButtonCommand old);

	Crontab findById(Integer id) throws SQLException;
	
	Crontab findByIdButton(Integer id) throws SQLException;
	
	void deleteCrontab(Crontab crontab);

}

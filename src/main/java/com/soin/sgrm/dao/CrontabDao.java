package com.soin.sgrm.dao;

import java.sql.SQLException;

import com.soin.sgrm.model.ButtonCommand;
import com.soin.sgrm.model.Crontab;

public interface CrontabDao {

	Crontab saveCrontab(Crontab crontab);

	Crontab updateCrontab(Crontab crontab, ButtonCommand old);

	Crontab findById(Integer id) throws SQLException;

	void deleteCrontab(Crontab crontab);

}

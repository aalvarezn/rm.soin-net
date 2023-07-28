package com.soin.sgrm.service.pos;

import java.sql.SQLException;

import com.soin.sgrm.model.pos.PButtonCommand;
import com.soin.sgrm.model.pos.PCrontab;

public interface PCrontabService {

	PCrontab saveCrontab(PCrontab crontab);

	PCrontab updateCrontab(PCrontab crontab, PButtonCommand old);

	PCrontab findById(Integer id) throws SQLException;
	
	PCrontab findByIdButton(Integer id) throws SQLException;
	
	void deleteCrontab(PCrontab crontab);

}

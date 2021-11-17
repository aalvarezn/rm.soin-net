package com.soin.sgrm.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ButtonCommandDao;
import com.soin.sgrm.model.ButtonCommand;

@Transactional("transactionManager")
@Service("ButtonCommandService")
public class ButtonCommandServiceImpl implements ButtonCommandService {

	@Autowired
	ButtonCommandDao dao;

	@Override
	public ButtonCommand saveButton(ButtonCommand button) throws SQLException {
		return dao.saveButton(button);
	}

	@Override
	public ButtonCommand findById(Integer id) throws SQLException {
		return dao.findById(id);
	}

	@Override
	public void deleteButton(ButtonCommand button) throws SQLException {
		dao.deleteButton(button);
		
	}

	@Override
	public ButtonCommand updateButton(ButtonCommand button) throws SQLException {
		return dao.updateButton(button);
	}

}

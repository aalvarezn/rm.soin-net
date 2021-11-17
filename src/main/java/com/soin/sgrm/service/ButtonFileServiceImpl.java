package com.soin.sgrm.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ButtonFileDao;
import com.soin.sgrm.model.ButtonFile;

@Transactional("transactionManager")
@Service("ButtonFileService")
public class ButtonFileServiceImpl implements ButtonFileService {

	@Autowired
	ButtonFileDao dao;

	@Override
	public ButtonFile saveButton(ButtonFile button) throws SQLException {
		return dao.saveButton(button);
	}

	@Override
	public ButtonFile updateButton(ButtonFile button) throws SQLException {
		return dao.updateButton(button);
	}

	@Override
	public ButtonFile findById(Integer id) throws SQLException {
		return dao.findById(id);
	}

	@Override
	public void deleteButton(ButtonFile button) throws SQLException {
		dao.deleteButton(button);

	}

}

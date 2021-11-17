package com.soin.sgrm.service;

import java.sql.SQLException;

import com.soin.sgrm.model.ButtonFile;

public interface ButtonFileService {
	
	ButtonFile saveButton(ButtonFile button) throws SQLException;

	ButtonFile updateButton(ButtonFile button) throws SQLException;

	ButtonFile findById(Integer id) throws SQLException;

	void deleteButton(ButtonFile button) throws SQLException;

}

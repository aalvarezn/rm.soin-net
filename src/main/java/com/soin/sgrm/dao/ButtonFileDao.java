package com.soin.sgrm.dao;

import java.sql.SQLException;

import com.soin.sgrm.model.ButtonFile;

public interface ButtonFileDao {

	ButtonFile saveButton(ButtonFile button);

	ButtonFile updateButton(ButtonFile button) throws SQLException;

	ButtonFile findById(Integer id) throws SQLException;

	void deleteButton(ButtonFile button) throws SQLException;

}

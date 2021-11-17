package com.soin.sgrm.service;

import java.sql.SQLException;

import com.soin.sgrm.model.ButtonCommand;

public interface ButtonCommandService {

	ButtonCommand saveButton(ButtonCommand button) throws SQLException;

	ButtonCommand updateButton(ButtonCommand button) throws SQLException;

	ButtonCommand findById(Integer id) throws SQLException;

	void deleteButton(ButtonCommand button) throws SQLException;

}

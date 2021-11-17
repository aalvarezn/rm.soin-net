package com.soin.sgrm.dao;

import java.sql.SQLException;

import com.soin.sgrm.model.ButtonCommand;

public interface ButtonCommandDao {

	ButtonCommand saveButton(ButtonCommand button) throws SQLException;
	
	ButtonCommand updateButton(ButtonCommand button) throws SQLException;
	
	ButtonCommand findById(Integer id) throws SQLException;
	
	void deleteButton(ButtonCommand button) throws SQLException;

}

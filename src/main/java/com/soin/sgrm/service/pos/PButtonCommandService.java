package com.soin.sgrm.service.pos;

import java.sql.SQLException;

import com.soin.sgrm.model.pos.PButtonCommand;



public interface PButtonCommandService {

	PButtonCommand saveButton(PButtonCommand button) throws SQLException;

	PButtonCommand updateButton(PButtonCommand button) throws SQLException;

	PButtonCommand findById(Integer id) throws SQLException;

	void deleteButton(PButtonCommand button) throws SQLException;

}

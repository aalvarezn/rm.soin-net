package com.soin.sgrm.service.pos;

import java.sql.SQLException;

import com.soin.sgrm.model.pos.PButtonFile;

public interface PButtonFileService {
	
	PButtonFile saveButton(PButtonFile button) throws SQLException;

	PButtonFile updateButton(PButtonFile button) throws SQLException;

	PButtonFile findById(Integer id) throws SQLException;

	void deleteButton(PButtonFile button) throws SQLException;

}

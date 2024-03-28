package com.soin.sgrm.dao.pos;

import java.sql.SQLException;

import com.soin.sgrm.model.pos.PButtonFile;



public interface PButtonFileDao {

	PButtonFile saveButton(PButtonFile button);

	PButtonFile updateButton(PButtonFile button) throws SQLException;

	PButtonFile findById(Integer id) throws SQLException;

	void deleteButton(PButtonFile button) throws SQLException;

}

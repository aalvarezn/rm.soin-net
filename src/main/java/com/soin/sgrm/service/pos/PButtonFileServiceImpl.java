package com.soin.sgrm.service.pos;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PButtonFileDao;
import com.soin.sgrm.model.pos.PButtonFile;

@Transactional("transactionManagerPos")
@Service("PButtonFileService")
public class PButtonFileServiceImpl implements PButtonFileService {

	@Autowired
	PButtonFileDao dao;

	@Override
	public PButtonFile saveButton(PButtonFile button) throws SQLException {
		return dao.saveButton(button);
	}

	@Override
	public PButtonFile updateButton(PButtonFile button) throws SQLException {
		return dao.updateButton(button);
	}

	@Override
	public PButtonFile findById(Integer id) throws SQLException {
		return dao.findById(id);
	}

	@Override
	public void deleteButton(PButtonFile button) throws SQLException {
		dao.deleteButton(button);

	}

}

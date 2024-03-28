package com.soin.sgrm.service.pos;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PButtonCommandDao;
import com.soin.sgrm.model.pos.PButtonCommand;

@Transactional("transactionManagerPos")
@Service("PButtonCommandService")
public class PButtonCommandServiceImpl implements PButtonCommandService {

	@Autowired
	PButtonCommandDao dao;

	@Override
	public PButtonCommand saveButton(PButtonCommand button) throws SQLException {
		return dao.saveButton(button);
	}

	@Override
	public PButtonCommand findById(Integer id) throws SQLException {
		return dao.findById(id);
	}

	@Override
	public void deleteButton(PButtonCommand button) throws SQLException {
		dao.deleteButton(button);
		
	}

	@Override
	public PButtonCommand updateButton(PButtonCommand button) throws SQLException {
		return dao.updateButton(button);
	}

}

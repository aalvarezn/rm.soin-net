package com.soin.sgrm.service.pos;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ModifiedComponentDao;
import com.soin.sgrm.dao.pos.PModifiedComponentDao;
import com.soin.sgrm.model.ModifiedComponent;
import com.soin.sgrm.model.pos.PModifiedComponent;

@Transactional("transactionManagerPos")
@Service("PModifiedComponentService")
public class PModifiedComponentServiceImpl implements PModifiedComponentService {

	@Autowired
	PModifiedComponentDao dao;

	@Override
	public List<PModifiedComponent> list() throws SQLException {
		return dao.list();
	}

	@Override
	public PModifiedComponent findById(Integer id) {
		return dao.findById(id);
	}

}

package com.soin.sgrm.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ModifiedComponentDao;
import com.soin.sgrm.dao.pos.PModifiedComponentDao;
import com.soin.sgrm.model.ModifiedComponent;
import com.soin.sgrm.model.pos.PModifiedComponent;

@Transactional("transactionManager")
@Service("ModifiedComponentService")
public class ModifiedComponentServiceImpl implements ModifiedComponentService {

	@Autowired
	ModifiedComponentDao dao;

	@Override
	public List<ModifiedComponent> list() throws SQLException {
		return dao.list();
	}

	@Override
	public ModifiedComponent findById(Integer id) {
		return dao.findById(id);
	}

}

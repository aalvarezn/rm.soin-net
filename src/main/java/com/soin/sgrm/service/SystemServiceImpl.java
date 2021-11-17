package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.SystemDao;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.SystemModule;
import com.soin.sgrm.model.SystemUser;
import com.soin.sgrm.utils.JsonSheet;

@Transactional("transactionManager")
@Service("SystemService")
public class SystemServiceImpl implements SystemService {

	@Autowired
	SystemDao dao;

	@Override
	public List<SystemUser> listSystemByUser(String name) {
		return dao.listSystemByUser(name);
	}

	@Override
	public Object[] myTeams(String name) {
		return dao.myTeams(name);
	}

	@Override
	public JsonSheet listSystem(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch) {
		return dao.listSystem(name, sEcho, iDisplayStart, iDisplayLength, sSearch);
	}

	@Override
	public SystemInfo findByCode(String code) throws Exception {
		return dao.findByCode(code);
	}

	@Override
	public SystemUser findSystemDocumentInfo(Integer systemId) throws Exception {
		return dao.findSystemDocumentInfo(systemId);
	}

	@Override
	public SystemModule findModuleBySystem(Integer systemId) throws Exception {
		return dao.findModuleBySystem(systemId);
	}

	@Override
	public List<SystemInfo> listAll() {
		return dao.listAll();
	}
}

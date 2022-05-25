package com.soin.sgrm.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.soin.sgrm.dao.SystemDao;
import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.SystemModule;
import com.soin.sgrm.model.SystemUser;
import com.soin.sgrm.model.System;
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

	@SuppressWarnings("rawtypes")
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

	@Override
	public SystemInfo findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public SystemUser findSystemUserById(Integer id) {
		return dao.findSystemUserById(id);
	}

	@Override
	public List<System> list() {
		return dao.list();
	}

	@Override
	public System findSystemById(Integer id) {
		return dao.findSystemById(id);
	}

	@Override
	public void save(System system) {
		dao.save(system);
	}

	@Override
	public void update(System system) {
		dao.update(system);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

	@Override
	public List<SystemUser> listSystemUser() {
		return dao.listSystemUser();
	}

	@Override
	public Object[] myTeamsProyect(String name) {
		return dao.myTeamsProyect(name);
	}

	@Override
	public List<SystemUser> listSystemUserByIds(Object[] ids) {
		return dao.listSystemUserByIds(ids);
	}

	@SuppressWarnings("unchecked")
	public List<System> listProjects(int id){
		return dao.listProjects(id);
	}
}

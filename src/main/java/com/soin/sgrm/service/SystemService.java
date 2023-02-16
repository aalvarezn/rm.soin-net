package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.SystemModule;
import com.soin.sgrm.model.SystemUser;
import com.soin.sgrm.model.System;
import com.soin.sgrm.utils.JsonSheet;

public interface SystemService {

	/* Metodos para la generacion de un relase, listado, edicion y resumen */

	List<SystemUser> listSystemByUser(String name);

	List<SystemUser> listSystemUser();
	
	List<SystemUser> listSystemUserByIds(Object[] ids);

	JsonSheet listSystem(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch);

	SystemInfo findByCode(String code) throws Exception;

	Object[] myTeams(String name);

	SystemUser findSystemDocumentInfo(Integer systemId) throws Exception;

	SystemModule findModuleBySystem(Integer systemId) throws Exception;

	List<SystemInfo> listAll();

	SystemInfo findById(Integer id);

	SystemUser findSystemUserById(Integer id);

	// ==================================================================================================

	List<System> list();

	System findSystemById(Integer id);

	SystemInfo findSystemInfoById(Integer id);
	void save(System system);

	void update(System system);

	void delete(Integer id);
	
	Object[] myTeamsProyect(String name);
	
	List<System> listProjects(int id);

	List<System> findByGroupIncidence(List<Long> listAttentionGroupId);

	List<System> findByUserIncidence(Integer userLogin);

	List<System> findByManagerIncidence(Integer idUser);
}

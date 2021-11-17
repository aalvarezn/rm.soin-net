package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.SystemInfo;
import com.soin.sgrm.model.SystemModule;
import com.soin.sgrm.model.SystemUser;
import com.soin.sgrm.utils.JsonSheet;

public interface SystemService {

	List<SystemUser> listSystemByUser(String name);
	JsonSheet listSystem(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch);
	
	SystemInfo findByCode(String code) throws Exception;
	
	Object[] myTeams(String name);
	
	SystemUser findSystemDocumentInfo(Integer systemId) throws Exception;
	
	SystemModule findModuleBySystem(Integer systemId) throws Exception;
	
	List<SystemInfo> listAll();
}

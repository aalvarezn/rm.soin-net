package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.model.pos.PSystemModule;
import com.soin.sgrm.model.pos.PSystemUser;
import com.soin.sgrm.utils.JsonSheet;

public interface PSystemService {

	/* Metodos para la generacion de un relase, listado, edicion y resumen */

	List<PSystemUser> listSystemByUser(String name);

	List<PSystemUser> listSystemUser();
	
	List<PSystemUser> listSystemUserByIds(Object[] ids);

	JsonSheet listSystem(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch);

	PSystemInfo findByCode(String code) throws Exception;

	Object[] myTeams(String name);

	PSystemUser findSystemDocumentInfo(Integer systemId) throws Exception;

	PSystemModule findModuleBySystem(Integer systemId) throws Exception;

	List<PSystemInfo> listAll();

	PSystemInfo findById(Integer id);

	PSystemUser findSystemUserById(Integer id);

	// ==================================================================================================

	List<PSystem> list();

	PSystem findSystemById(Integer id);

	PSystemInfo findSystemInfoById(Integer id);
	void save(PSystem system);

	void update(PSystem system);

	void delete(Integer id);
	
	Object[] myTeamsProyect(String name);
	
	List<PSystem> listProjects(int id);


	List<PSystem> findByGroupIncidence(List<Long> listAttentionGroupId);

	List<PSystem> findByUserIncidence(Integer userLogin);

	List<PSystem> findByManagerIncidence(Integer idUser);

	List<PSystem> getSystemByProject(Integer projectId);

}

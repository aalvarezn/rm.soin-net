package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.soin.sgrm.dao.pos.PSystemDao;
import com.soin.sgrm.model.pos.PSystem;
import com.soin.sgrm.model.pos.PSystemInfo;
import com.soin.sgrm.model.pos.PSystemModule;
import com.soin.sgrm.model.pos.PSystemUser;
import com.soin.sgrm.utils.JsonSheet;

@Transactional("transactionManagerPos")
@Service("PSystemService")
public class PSystemServiceImpl implements PSystemService {

	@Autowired
	PSystemDao dao;

	@Override
	public List<PSystemUser> listSystemByUser(String name) {
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
	public PSystemInfo findByCode(String code) throws Exception {
		return dao.findByCode(code);
	}

	@Override
	public PSystemUser findSystemDocumentInfo(Integer systemId) throws Exception {
		return dao.findSystemDocumentInfo(systemId);
	}

	@Override
	public PSystemModule findModuleBySystem(Integer systemId) throws Exception {
		return dao.findModuleBySystem(systemId);
	}

	@Override
	public List<PSystemInfo> listAll() {
		return dao.listAll();
	}

	@Override
	public PSystemInfo findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public PSystemUser findSystemUserById(Integer id) {
		return dao.findSystemUserById(id);
	}

	@Override
	public List<PSystem> list() {
		return dao.list();
	}

	@Override
	public PSystem findSystemById(Integer id) {
		return dao.findSystemById(id);
	}

	@Override
	public void save(PSystem system) {
		dao.save(system);
	}

	@Override
	public void update(PSystem system) {
		dao.update(system);
	}

	@Override
	public void delete(Integer id) {
		dao.delete(id);
	}

	@Override
	public List<PSystemUser> listSystemUser() {
		return dao.listSystemUser();
	}

	@Override
	public Object[] myTeamsProyect(String name) {
		return dao.myTeamsProyect(name);
	}

	@Override
	public List<PSystemUser> listSystemUserByIds(Object[] ids) {
		return dao.listSystemUserByIds(ids);
	}

	@Override
	public List<PSystem> listProjects(int id) {
		return dao.listProjects(id);
	}

	@Override
	public PSystemInfo findSystemInfoById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public List<PSystem> findByGroupIncidence(List<Long> listAttentionGroupId) {
		return dao.findByGroupIncidence(listAttentionGroupId);
	}

	@Override
	public List<PSystem> findByUserIncidence(Integer userLogin) {
		return dao.findByUserIncidence(userLogin);
	}

	@Override
	public List<PSystem> findByManagerIncidence(Integer idUser) {
		// TODO Auto-generated method stub
		return dao.findByManagerIncidence(idUser);
	}

	@Override
	public List<PSystem> getSystemByProject(Integer projectId) {
		// TODO Auto-generated method stub
		return dao.getSystemByProject(projectId);
	}
}
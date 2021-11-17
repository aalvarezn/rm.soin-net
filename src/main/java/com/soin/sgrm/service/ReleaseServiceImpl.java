package com.soin.sgrm.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ReleaseDao;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.ReleaseObjectEdit;
import com.soin.sgrm.model.Risk;
import com.soin.sgrm.model.Status;
import com.soin.sgrm.model.ReleaseSummary;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.UserInfo;
import com.soin.sgrm.utils.JsonSheet;
import com.soin.sgrm.utils.ReleaseCreate;

@Transactional("transactionManager")
@Service("ReleaseService")
public class ReleaseServiceImpl implements ReleaseService {

	@Autowired
	ReleaseDao dao;

	@Override
	public ReleaseSummary findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public Integer countByType(String name, String type, int query, Object[] ids) {
		return dao.countByType(name, type, query, ids);
	}

	@Override
	public JsonSheet<?> listByUser(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch)
			throws SQLException {
		return dao.listByUser(name, sEcho, iDisplayStart, iDisplayLength, sSearch);
	}

	@Override
	public JsonSheet<?> listByTeams(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			Object[] ids) throws SQLException {
		return dao.listByTeams(name, sEcho, iDisplayStart, iDisplayLength, sSearch, ids);
	}

	@Override
	public JsonSheet<?> listByAllSystem(String name, int sEcho, int iDisplayStart, int iDisplayLength, String sSearch)
			throws SQLException {
		return dao.listByAllSystem(name, sEcho, iDisplayStart, iDisplayLength, sSearch);
	}

	@Override
	public Integer existNumRelease(String number_release) throws SQLException {
		return dao.existNumRelease(number_release);
	}

	@Override
	public ReleaseEdit findEditById(Integer id) throws SQLException {
		return dao.findEditById(id);
	}

	@Override
	public void save(Release release, String tpos) throws Exception {
		dao.save(release, tpos);

	}

	@Override
	public List<ReleaseUser> list(String search, String release_id) throws SQLException {
		return dao.list(search, release_id);
	}

	@Override
	public void cancelRelease(ReleaseEdit release) throws Exception {
		dao.cancelRelease(release);
	}

	@Override
	public Release findReleaseById(Integer id) throws SQLException {
		return dao.findReleaseById(id);
	}

	@Override
	public void saveRelease(Release release, ReleaseCreate rc) throws Exception {
		dao.saveRelease(release, rc);
	}

	@Override
	public void requestRelease(Release release) throws SQLException {
		dao.requestRelease(release);
	}

	@Override
	public ArrayList<ReleaseObjectEdit> saveReleaseObjects(Integer release_id, ArrayList<ReleaseObjectEdit> objects) {
		return dao.saveReleaseObjects(release_id, objects);
	}

	@Override
	public void copy(ReleaseEdit release, String tpos) throws Exception {
		dao.copy(release, tpos);
	}

	@Override
	public void assignRelease(ReleaseEdit release, UserInfo user) throws SQLException {
		dao.assignRelease(release, user);
	}

	@Override
	public ReleaseUser findReleaseUserById(Integer id) throws SQLException {
		return dao.findReleaseUserById(id);
	}

}

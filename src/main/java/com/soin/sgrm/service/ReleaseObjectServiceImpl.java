package com.soin.sgrm.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gdata.util.ParseException;
import com.soin.sgrm.dao.ReleaseObjectDao;
import com.soin.sgrm.model.Dependency;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.ReleaseObjectEdit;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.Release_Objects;
import com.soin.sgrm.utils.JsonSheet;

@Transactional("transactionManager")
@Service("ReleaseObjectService")
public class ReleaseObjectServiceImpl implements ReleaseObjectService {

	@Autowired
	ReleaseObjectDao dao;

	@Override
	public ReleaseObjectEdit saveObject(ReleaseObjectEdit rObj, Release release) throws Exception {
		return dao.saveObject(rObj, release);

	}

	@Override
	public ReleaseObjectEdit findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void deleteObject(Integer releaseId, ReleaseObject object) throws Exception {
		dao.deleteObject(releaseId, object);
	}

	@Override
	public ReleaseUser findReleaseToAddByObject(ReleaseObjectEdit obj, Release release) {
		return dao.findReleaseToAddByObject(obj, release);
	}

	@Override
	public ReleaseUser findReleaseToDeleteByObject(Release release, ReleaseObject obj) {
		return dao.findReleaseToDeleteByObject(release, obj);
	}

	@Override
	public List<Object[]> findReleaseToAddByObjectList(ArrayList<ReleaseObjectEdit> objects, ReleaseEdit release) {
		return dao.findReleaseToAddByObjectList(objects, release);
	}

	@Override
	public List<Object[]> findCoDependencies(ArrayList<ReleaseObject> objects, ReleaseUser release) {
		return dao.findCoDependencies(objects, release);
	}

	@Override
	public JsonSheet<?> listObjectsByReleases(int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			Integer releaseId,Integer sql) throws SQLException, ParseException{
		return dao.listObjectsByReleases(sEcho,iDisplayStart,iDisplayLength,sSearch,releaseId,sql);
	}

	@Override
	public Integer listCountByReleases(Integer releaseId) throws ParseException, SQLException {
		return dao.listCountByReleases(releaseId);
	}

	@Override
	public List<Release_Objects> listObjectsSql(Integer idRelease) {
		return dao.listObjectsSql(idRelease);
	}

}

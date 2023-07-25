package com.soin.sgrm.service.pos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gdata.util.ParseException;
import com.soin.sgrm.dao.pos.PReleaseObjectDao;
import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.model.pos.PReleaseEdit;
import com.soin.sgrm.model.pos.PReleaseObject;
import com.soin.sgrm.model.pos.PReleaseObjectEdit;
import com.soin.sgrm.model.pos.PReleaseUser;
import com.soin.sgrm.model.pos.PRelease_Objects;
import com.soin.sgrm.utils.JsonSheet;

@Transactional("transactionManagerPos")
@Service("PReleaseObjectService")
public class PReleaseObjectServiceImpl implements PReleaseObjectService {

	@Autowired
	PReleaseObjectDao dao;

	@Override
	public PReleaseObjectEdit saveObject(PReleaseObjectEdit rObj, PRelease release) throws Exception {
		return dao.saveObject(rObj, release);

	}

	@Override
	public PReleaseObjectEdit findById(Integer id) {
		return dao.findById(id);
	}

	@Override
	public void deleteObject(Integer releaseId, PReleaseObject object) throws Exception {
		dao.deleteObject(releaseId, object);
	}

	@Override
	public PReleaseUser findReleaseToAddByObject(PReleaseObjectEdit obj, PRelease release) {
		return dao.findReleaseToAddByObject(obj, release);
	}

	@Override
	public PReleaseUser findReleaseToDeleteByObject(PRelease release, PReleaseObject obj) {
		return dao.findReleaseToDeleteByObject(release, obj);
	}

	@Override
	public List<Object[]> findReleaseToAddByObjectList(ArrayList<PReleaseObjectEdit> objects, PReleaseEdit release) {
		return dao.findReleaseToAddByObjectList(objects, release);
	}

	@Override
	public List<Object[]> findCoDependencies(ArrayList<PReleaseObject> objects, PReleaseUser release) {
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
	public List<PRelease_Objects> listObjectsSql(Integer idRelease) {
		return dao.listObjectsSql(idRelease);
	}

}

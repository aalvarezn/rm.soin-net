package com.soin.sgrm.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gdata.util.ParseException;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.ReleaseObjectEdit;
import com.soin.sgrm.model.ReleaseUser;
import com.soin.sgrm.model.Release_Objects;
import com.soin.sgrm.utils.JsonSheet;


public interface ReleaseObjectDao {

	ReleaseUser findReleaseToAddByObject(ReleaseObjectEdit obj, Release release);

	ReleaseUser findReleaseToDeleteByObject(Release release, ReleaseObject obj);

	ReleaseObjectEdit saveObject(ReleaseObjectEdit rObj, Release release) throws Exception;

	ReleaseObjectEdit findById(Integer id);

	void deleteObject(Integer releaseId, ReleaseObject object) throws Exception;
	
	List<Object[]> findReleaseToAddByObjectList(ArrayList<ReleaseObjectEdit> objects, ReleaseEdit release);
	
	List<Object[]> findCoDependencies(ArrayList<ReleaseObject> objects, ReleaseUser release);
	
	JsonSheet<?> listObjectsByReleases(int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			Integer releaseId,Integer sql) throws SQLException, ParseException;

	Integer listCountByReleases(Integer releaseId) throws ParseException, SQLException;

	List<Release_Objects> listObjectsSql(Integer idRelease);

	List<Release_Objects> listObjects(Integer idRelease);
}

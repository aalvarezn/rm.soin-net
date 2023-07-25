package com.soin.sgrm.service.pos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gdata.util.ParseException;
import com.soin.sgrm.model.pos.PRelease;
import com.soin.sgrm.model.pos.PReleaseEdit;
import com.soin.sgrm.model.pos.PReleaseObject;
import com.soin.sgrm.model.pos.PReleaseObjectEdit;
import com.soin.sgrm.model.pos.PReleaseUser;
import com.soin.sgrm.model.pos.PRelease_Objects;
import com.soin.sgrm.utils.JsonSheet;

public interface PReleaseObjectService {

	PReleaseUser findReleaseToAddByObject(PReleaseObjectEdit obj, PRelease release);

	PReleaseUser findReleaseToDeleteByObject(PRelease release, PReleaseObject obj);

	PReleaseObjectEdit saveObject(PReleaseObjectEdit rObj, PRelease release) throws Exception;

	PReleaseObjectEdit findById(Integer id);

	void deleteObject(Integer releaseId, PReleaseObject object) throws Exception;

	List<Object[]> findReleaseToAddByObjectList(ArrayList<PReleaseObjectEdit> objects, PReleaseEdit release);

	List<Object[]> findCoDependencies(ArrayList<PReleaseObject> objects, PReleaseUser release);
	
	JsonSheet<?> listObjectsByReleases(int sEcho, int iDisplayStart, int iDisplayLength, String sSearch,
			Integer releaseId,Integer sql) throws SQLException, ParseException;

	Integer listCountByReleases(Integer releaseId) throws ParseException, SQLException;

	List<PRelease_Objects> listObjectsSql(Integer idRelease);
}

package com.soin.sgrm.dao;

import java.util.ArrayList;
import java.util.List;

import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.ReleaseObjectEdit;
import com.soin.sgrm.model.ReleaseUser;

public interface ReleaseObjectDao {

	ReleaseUser findReleaseToAddByObject(ReleaseObjectEdit obj, Release release);

	ReleaseUser findReleaseToDeleteByObject(Release release, ReleaseObject obj);

	ReleaseObjectEdit saveObject(ReleaseObjectEdit rObj, Release release) throws Exception;

	ReleaseObjectEdit findById(Integer id);

	void deleteObject(Integer releaseId, ReleaseObject object) throws Exception;
	
	List<Object[]> findReleaseToAddByObjectList(ArrayList<ReleaseObjectEdit> objects, ReleaseEdit release);
	
	List<Object[]> findCoDependencies(ArrayList<ReleaseObject> objects, ReleaseUser release);

}

package com.soin.sgrm.dao;

import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.ReleaseObjectEdit;

public interface ReleaseObjectDao {

	Release findReleaseToAddByObject(ReleaseObjectEdit obj, Release release);

	Release findReleaseToDeleteByObject(Release release, ReleaseObject obj);

	ReleaseObjectEdit saveObject(ReleaseObjectEdit rObj, Release release) throws Exception;

	ReleaseObjectEdit findById(Integer id);

	void deleteObject(Integer releaseId, ReleaseObject object) throws Exception;

}

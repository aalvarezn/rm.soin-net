package com.soin.sgrm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ReleaseObjectDao;
import com.soin.sgrm.model.Dependency;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseObject;
import com.soin.sgrm.model.ReleaseObjectEdit;

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
	public Release findReleaseToAddByObject(ReleaseObjectEdit obj, Release release) {
		return dao.findReleaseToAddByObject(obj, release);
	}

	@Override
	public Release findReleaseToDeleteByObject(Release release, ReleaseObject obj) {
		return dao.findReleaseToDeleteByObject(release, obj);
	}

}

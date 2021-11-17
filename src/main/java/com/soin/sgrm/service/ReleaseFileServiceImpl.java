package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.ReleaseFileDao;
import com.soin.sgrm.model.ReleaseFile;

@Transactional("transactionManager")
@Service("ReleaseFileService")
public class ReleaseFileServiceImpl implements ReleaseFileService {

	@Autowired
	ReleaseFileDao releaseDao;

	@Override
	public void save(Integer id, ReleaseFile releaseFile) throws Exception {
		releaseDao.save(id, releaseFile);
	}

	@Override
	public ReleaseFile findReleaseFile(String path) {
		return releaseDao.findReleaseFile(path);
	}

	@Override
	public ReleaseFile findReleaseFileById(Integer id) {
		return releaseDao.findReleaseFileById(id);
	}

	@Override
	public void deleteReleaseFile(ReleaseFile releaseFile) throws Exception {
		releaseDao.deleteReleaseFile(releaseFile);
	}

	@Override
	public List<String> ImpactObjects(Integer release_id) {
		return releaseDao.ImpactObjects(release_id);
	}

}

package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.pos.PReleaseFileDao;
import com.soin.sgrm.model.pos.PReleaseFile;

@Transactional("transactionManagerPos")
@Service("PReleaseFileService")
public class PReleaseFileServiceImpl implements PReleaseFileService {

	@Autowired
	PReleaseFileDao releaseDao;

	@Override
	public void save(Integer id, PReleaseFile releaseFile) throws Exception {
		releaseDao.save(id, releaseFile);
	}

	@Override
	public PReleaseFile findReleaseFile(String path) {
		return releaseDao.findReleaseFile(path);
	}

	@Override
	public PReleaseFile findReleaseFileById(Integer id) {
		return releaseDao.findReleaseFileById(id);
	}

	@Override
	public void deleteReleaseFile(PReleaseFile releaseFile) throws Exception {
		releaseDao.deleteReleaseFile(releaseFile);
	}

	@Override
	public List<String> ImpactObjects(Integer release_id) {
		return releaseDao.ImpactObjects(release_id);
	}

}

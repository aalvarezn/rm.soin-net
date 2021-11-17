package com.soin.sgrm.service.corp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.corp.RMReleaseFileDao;
import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.corp.RMReleaseFile;

@Transactional("transactionManagerCorp")
@Service("RMReleaseFileService")
public class RMReleaseFileServiceImpl implements RMReleaseFileService {

	@Autowired
	RMReleaseFileDao dao;

	@Override
	public List<RMReleaseFile> listByRelease(ReleaseEdit release) {
		return dao.listByRelease(release);
	}

	@Override
	public RMReleaseFile findByRelease(String release) {
		return dao.findByRelease(release);
	}

}

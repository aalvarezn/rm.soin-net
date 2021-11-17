package com.soin.sgrm.service.corp;

import java.util.List;

import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.corp.RMReleaseFile;

public interface RMReleaseFileService {

	List<RMReleaseFile> listByRelease(ReleaseEdit release);
	RMReleaseFile findByRelease(String release);

}

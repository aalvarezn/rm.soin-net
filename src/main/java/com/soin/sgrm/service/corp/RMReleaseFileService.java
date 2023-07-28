package com.soin.sgrm.service.corp;

import java.util.List;

import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.corp.RMReleaseFile;
import com.soin.sgrm.model.pos.PReleaseEdit;

public interface RMReleaseFileService {

	List<RMReleaseFile> listByRelease(ReleaseEdit release);
	List<RMReleaseFile> listByRelease(PReleaseEdit release);
	RMReleaseFile findByRelease(String release);

}

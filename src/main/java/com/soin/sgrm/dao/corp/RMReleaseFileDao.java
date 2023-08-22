package com.soin.sgrm.dao.corp;

import java.util.List;

import com.soin.sgrm.model.Release;
import com.soin.sgrm.model.ReleaseEdit;
import com.soin.sgrm.model.corp.RMReleaseFile;
import com.soin.sgrm.model.pos.PReleaseEdit;

public interface RMReleaseFileDao {
	
	List<RMReleaseFile> listByRelease(ReleaseEdit release);
	RMReleaseFile findByRelease(String release);
	List<RMReleaseFile> listByRelease(PReleaseEdit release);

}

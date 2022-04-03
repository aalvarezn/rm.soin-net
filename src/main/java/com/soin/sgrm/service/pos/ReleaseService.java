package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PRelease;

public interface ReleaseService  extends BaseService<Long, PRelease>{
	 List<PRelease> listReleasesBySystem(Long id);
}

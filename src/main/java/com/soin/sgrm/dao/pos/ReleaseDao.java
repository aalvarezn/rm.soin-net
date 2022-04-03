package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PRelease;

public interface ReleaseDao extends BaseDao<Long, PRelease> {
	 List<PRelease> listReleasesBySystem(Long id);
}

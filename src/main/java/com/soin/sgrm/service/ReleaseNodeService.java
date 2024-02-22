package com.soin.sgrm.service;

import com.soin.sgrm.model.ReleaseNode;

public interface ReleaseNodeService extends BaseService<Long, ReleaseNode>{

	public boolean findReleaseNode(Integer idRelease,Integer idNode);

}
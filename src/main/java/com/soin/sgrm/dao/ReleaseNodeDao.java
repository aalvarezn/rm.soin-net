package com.soin.sgrm.dao;

import com.soin.sgrm.model.ReleaseNode;

public interface ReleaseNodeDao extends BaseDao<Long, ReleaseNode>{

	public boolean findReleaseNode(Integer idRelease,Integer idNode);


}


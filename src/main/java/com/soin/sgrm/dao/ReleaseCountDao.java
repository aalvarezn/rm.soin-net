package com.soin.sgrm.dao;

import java.util.List;

import com.soin.sgrm.model.ReleaseCount;

public interface ReleaseCountDao extends BaseDao<Long, ReleaseCount> {

	List<ReleaseCount> findAllList(String[] releaseName, String status, String dateRange);

}

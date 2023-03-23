package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.model.ReleaseCount;

public interface ReleaseCountService extends BaseService<Long, ReleaseCount>{


	List<ReleaseCount> findAllList(String[] releaseName,String status, String dateRange);

}
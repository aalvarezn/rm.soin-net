package com.soin.sgrm.dao;

import java.util.List;

import javax.persistence.QueryHint;

import com.soin.sgrm.utils.ReleaseByTime;

public interface StatisticDao {
	
	List<ReleaseByTime> getLastFourYears();
	
	List<ReleaseByTime> getLastMonth();
	
}

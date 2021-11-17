package com.soin.sgrm.service;

import java.util.List;

import com.soin.sgrm.utils.ReleaseByTime;

public interface StatisticService {
	
	List<ReleaseByTime> getLastFourYears();
	
	List<ReleaseByTime> getLastMonth();

}

package com.soin.sgrm.service.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PReleaseByTime;


public interface PStatisticService {
	
	List<PReleaseByTime> getLastFourYears();
	
	List<PReleaseByTime> getLastMonth();

}

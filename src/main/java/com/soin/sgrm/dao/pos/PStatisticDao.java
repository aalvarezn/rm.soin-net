package com.soin.sgrm.dao.pos;

import java.util.List;

import com.soin.sgrm.model.pos.PReleaseByTime;

public interface PStatisticDao {
	
	List<PReleaseByTime> getLastFourYears();
	
	List<PReleaseByTime> getLastMonth();
	
}

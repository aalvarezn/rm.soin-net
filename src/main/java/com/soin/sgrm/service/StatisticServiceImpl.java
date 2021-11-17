package com.soin.sgrm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.StatisticDao;
import com.soin.sgrm.utils.ReleaseByTime;

@Transactional("transactionManager")
@Service("StatisticService")
public class StatisticServiceImpl implements StatisticService {
	
	@Autowired
	StatisticDao statisticDao;

	@Override
	public List<ReleaseByTime> getLastFourYears() {
		return statisticDao.getLastFourYears();
	}

	@Override
	public List<ReleaseByTime> getLastMonth() {
		// TODO Auto-generated method stub
		return statisticDao.getLastMonth();
	}

}

package com.soin.sgrm.service.pos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soin.sgrm.dao.StatisticDao;
import com.soin.sgrm.dao.pos.PStatisticDao;
import com.soin.sgrm.model.pos.PReleaseByTime;
import com.soin.sgrm.utils.ReleaseByTime;

@Transactional("transactionManagerPos")
@Service("PStatisticService")
public class PStatisticServiceImpl implements PStatisticService {
	
	@Autowired
	PStatisticDao statisticDao;

	@Override
	public List<PReleaseByTime> getLastFourYears() {
		return statisticDao.getLastFourYears();
	}

	@Override
	public List<PReleaseByTime> getLastMonth() {
		return statisticDao.getLastMonth();
	}

}

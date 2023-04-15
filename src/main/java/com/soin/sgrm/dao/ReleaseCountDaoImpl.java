package com.soin.sgrm.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.soin.sgrm.model.ReleaseCount;
import com.soin.sgrm.model.ReleaseTracking;

@Repository
public class ReleaseCountDaoImpl extends AbstractDao<Long, ReleaseCount> implements ReleaseCountDao{

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public List<ReleaseCount> findAllList(String[] releaseName, String status, String dateRange) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(ReleaseTracking.class);
		List<ReleaseCount> listReleaseCount= new ArrayList<ReleaseCount>();
		crit.createAlias("releaseName","release.name");
		Map<String, Object> columns = new HashMap<String, Object>();
		for(String release:releaseName){
		 ReleaseCount releaseCount=new ReleaseCount();
		 releaseCount.setNameRelease(release);
		 crit.add(Restrictions.eq("releaseName", release));
		 String[] range = (dateRange != null) ? dateRange.split("-") : null;
			if (range != null) {
				if (range.length > 1) {
					try {
						Date start = new SimpleDateFormat("dd/MM/yyyy").parse(range[0]);
						Date end = new SimpleDateFormat("dd/MM/yyyy").parse(range[1]);
						end.setHours(23);
						end.setMinutes(59);
						end.setSeconds(59);
						columns.put("trackingDate", Restrictions.between("trackingDate", start, end));
						
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
			for (Map.Entry<String, Object> column : columns.entrySet())
				crit = addFilters(crit, columns);
			Integer count = (Integer) crit.uniqueResult();
			releaseCount.setCount(count);
			releaseCount.setStatus(status);
			listReleaseCount.add(releaseCount);
			
		}
		return listReleaseCount;
	}

}

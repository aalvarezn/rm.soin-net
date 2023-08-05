package com.soin.sgrm.dao.pos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;


import com.soin.sgrm.model.pos.PRequestBase;
import com.soin.sgrm.model.pos.PRequestBaseR1;
import com.soin.sgrm.model.pos.PRequestBaseTrackingToError;
import com.soin.sgrm.model.pos.PRequestReport;
import com.soin.sgrm.model.pos.PSystemInfo;

@Repository
public class PRequestBaseDaoImpl extends AbstractDao<Long, PRequestBase> implements PRequestBaseDao{

	@Autowired
	@Qualifier("sessionFactoryPos")
	private SessionFactory sessionFactory;

	@Override
	public Integer existNumRequest(String numRequest) {
		Criteria crit = getSession().createCriteria(PRequestBase.class);
		crit.add(Restrictions.like("numRequest", numRequest, MatchMode.ANYWHERE));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		int recordsTotal = count.intValue();
		return recordsTotal;

	}
	@SuppressWarnings("unchecked")
	@Override
	public Integer countByType(Integer id, String type, int query, Object[] ids) {
		Criteria crit = getSession().createCriteria(PRequestBase.class);
		crit.createAlias("user", "user");
		crit.createAlias("status", "status");
		switch (query) {
		case 1:
			// query #1 Obtiene mis rfc
			List<PSystemInfo> systems = sessionFactory.getCurrentSession().createCriteria(PSystemInfo.class)
			.createAlias("managers","managers")
			.add(Restrictions.eq("managers.id", id)).list();
			List<Integer> listaId=new ArrayList<Integer>();
			for(PSystemInfo system: systems) {
				listaId.add(system.getId());
			}
			crit.createAlias("systemInfo", "systemInfo");
			crit.add(Restrictions.in("systemInfo.id", listaId));

			
			crit.add(Restrictions.eq("status.name", type));
			break;
		case 2:
			crit.add(Restrictions.eq("status.name", type));
		
		default:
			break;
		}
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return count.intValue();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Integer countByManager(Integer id, Long idRequest) {
		Criteria crit = getSession().createCriteria(PRequestBase.class);
		crit.createAlias("user", "user");
		crit.createAlias("status", "status");

			// query #1 Obtiene mis request
			List<PSystemInfo> systems = sessionFactory.getCurrentSession().createCriteria(PSystemInfo.class)
			.createAlias("managers","managers")
			.add(Restrictions.eq("managers.id", id)).list();
			List<Integer> listaId=new ArrayList<Integer>();
			for(PSystemInfo system: systems) {
				listaId.add(system.getId());
			}
			crit.createAlias("systemInfo", "systemInfo");
			crit.add(Restrictions.in("systemInfo.id", listaId));
			crit.add(Restrictions.eq("id", idRequest));

		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();
		return count.intValue();
	}
	@Override
	public PRequestBaseR1 getByIdR1(Long id) {
	    return (PRequestBaseR1) getSession().createCriteria(PRequestBaseR1.class)
	    		.add(Restrictions.eq("id", id))
	    		.uniqueResult();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<PRequestBaseTrackingToError> listByAllSystemError(String dateRange, int systemId) {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(PRequestBaseTrackingToError.class);
		crit.createAlias("request", "request");
		crit.createAlias("request.system", "system");
		String[] range = (dateRange != null) ? dateRange.split("-") : null;
		if (range != null) {
			if (range.length > 1) {
				try {
					Date start = new SimpleDateFormat("dd/MM/yyyy").parse(range[0]);
					Date end = new SimpleDateFormat("dd/MM/yyyy").parse(range[1]);
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);
					crit.add(Restrictions.ge("trackingDate", start));
					crit.add(Restrictions.le("trackingDate", end));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		if (systemId != 0) {
			crit.add(Restrictions.eq("system.id", systemId));
		}
		crit.add(Restrictions.eq("status", "Solicitado"));
		crit.addOrder(Order.desc("trackingDate"));

		return crit.list();
	}
	@Override
	public PRequestReport findByReport(Long id) {
		  return (PRequestReport) getSession().createCriteria(PRequestReport.class)
		    		.add(Restrictions.eq("id", id))
		    		.uniqueResult();
	}
}
